import {Component, OnInit} from '@angular/core';
import {Hall} from '../../dtos/createEvent/hall';
import {Artist} from '../../dtos/createEvent/artist';
import {Event} from '../../dtos/createEvent/event';
import {SelectPerformance} from '../../dtos/createEvent/selectPerformance';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {HallService} from '../../services/hall.service';
import {ArtistService} from '../../services/artist.service';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {EventService} from '../../services/event.service';
import {CreateEvent} from '../../dtos/createEvent/createEvent';
import {Sector} from '../../dtos/sector';
import {SectorService} from '../../services/sector.service';
import {FileUploadService} from '../../services/file-upload.service';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss']
})
export class EventComponent implements OnInit {
  events: Event = new Event();
  eventForm: FormGroup;

  performanceCounter = 0;
  artistCounter = 0;
  submitted = false;

  //load data from backend
  allArtists: Artist[] = [];
  allHalls: Hall[] = [];

  //load sectors when selecting hall
  sectors: Sector[] = [];


  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private hallService: HallService,
    private artistService: ArtistService,
    private eventService: EventService,
    private fileUploadService: FileUploadService,
    private authService: AuthService,
    private sectorService: SectorService,
    private notification: ToastrService,
    private formBuilder: FormBuilder
  ) {
    //

    this.eventForm = this.formBuilder.group({
      eventName: ['', [Validators.required]],
      type: ['', [Validators.required]],
      length: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      description: ['', [Validators.required]],
      image: [null, [Validators.required]],
      performances: this.formBuilder.array([
        this.formBuilder.group({
          // Define the form controls for each performance
          // Example:
          hall: ['', [Validators.required]],
          date: ['', [Validators.required]],
          sectorPrices: this.formBuilder.array([])
        })
      ], [Validators.required]),
      artists: this.formBuilder.array([], [Validators.required])
    });


  }

  ngOnInit(): void {
    this.hallService.getHalls().subscribe({
      next: data => {
        this.allHalls = data;
      }, error: error => {
        console.log(error);
        this.notification.error('Something went wrong');
      },
    });
    this.artistService.getArtists().subscribe({
      next: data => {
        this.allArtists = data;
      }, error: error => {
        console.log(error);
        this.notification.error('Something went wrong');
      },
    });

  }

  checkValid() {
    const controls = this.eventForm.controls;
    let isValid = true;

    Object.keys(controls).forEach((key: string) => {
      const control = controls[key];
      control.markAsTouched(); // Mark the control as touched to display validation errors
      if (control.invalid) {
        isValid = false;
      }
    });

    return isValid;
  }

  onFileSelected(event: any): void {
    if (event != null) {
      this.selectedFile = event.target.files[0];
    }
    if (this.selectedFile != null) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }


  submit() {
    console.log('sending eventForm');
    console.log(this.eventForm.controls);

    const image: File = this.eventForm.get('length').value;
    console.log('image');
    console.log(image);

    this.submitted = true;
    // Handle form submission logic here
    if (this.eventForm.valid && this.checkValid()) {

      // Create a new event object from the form fields
      const event = new Event();
      event.name = this.eventForm.get('eventName').value;
      event.type = this.eventForm.get('type').value;
      event.length = this.eventForm.get('length').value;
      event.description = this.eventForm.get('description').value;
      event.artists = this.eventForm.get('artists').value.map(artist => artist.artistName).join(';');
      event.imagePath = this.selectedFile.name;


      // Create a new performance object from the form fields
      const performances: SelectPerformance[] = [];
      const tmp = this.eventForm.get('performances');
      console.log('tmp');
      console.log(tmp.value);
      tmp.value.forEach(performance => {
        const perf = new SelectPerformance();
        perf.hallId = Number(performance.hall);
        perf.dateTime = performance.date;
        perf.sectorPrices = performance.sectorPrices;
        performances.push(perf);
      });
      console.log('performances');
      console.log(performances);

      //create new CreateEvent object
      const createEvent = new CreateEvent();
      createEvent.event = event;
      createEvent.performances = performances;

      console.log('sending DTO');
      console.log(createEvent);

      this.fileUploadService.uploadImage(this.selectedFile).subscribe({
        next: fileResponse => {
          console.log('receiving');
          console.log(fileResponse);
          createEvent.event.imagePath = fileResponse.imagePath;
          this.eventService.createEvent(createEvent).subscribe({
            next: eventResponse => {
              console.log('receiving');
              console.log(eventResponse);
              this.notification.success('Event created successfully');
              this.router.navigate(['/events/' + eventResponse.id]);
            }, error: error => {
              this.notification.error(error.error.detail);
            },
          });
          this.notification.success('Image uploaded successfully');
        }, error: error => {
          this.notification.error(error.error.detail);
        }
      });
    } else {
      this.notification.info('Please fill out all fields!');
    }

  }

  incNumPerformances() {
    const performances = this.eventForm.get('performances') as FormArray;
    performances.push(this.formBuilder.group({
      hall: [-1, [Validators.required]],
      date: ['', [Validators.required]],
      sectorPrices: [[], [Validators.required]] // Initialize sectorPrices as an empty array
    }));
    this.performanceCounter++;
  }

  incNumArtists() {
    const artists = this.eventForm.get('artists') as FormArray;
    artists.push(this.formBuilder.group({
      artistName: ['', [Validators.required]]
    }));
    this.artistCounter++;
  }

  delPerformance(index: number) {
    const performances = this.eventForm.get('performances') as FormArray;
    performances.removeAt(index);
    this.performanceCounter--;
  }

  delArtist(index: number) {
    const artists = this.eventForm.get('artists') as FormArray;
    artists.removeAt(index);
    this.artistCounter--;
  }

  getPerformances() {
    const performancesFormArray = this.eventForm.get('performances') as FormArray;
    return performancesFormArray;
  }

  getSectors(index: number) {
    const performancesFormArray = this.eventForm.get('performances') as FormArray;

    const performanceFormGroup = performancesFormArray.at(index) as FormGroup;
    const hall = performanceFormGroup.get('sectorPrices') as FormArray;
    return hall;
  }

  getArtist() {
    const performancesFormArray = this.eventForm.get('artists') as FormArray;
    return performancesFormArray;
  }


  loadSectors(id) {
    if (id === undefined || id === null) {
      this.sectors = [];
      this.notification.info('Please select a hall');
      return;
    }
    this.sectorService.getSectorFromHall(id).subscribe({
      next: data => {
        console.log(data);
        const format = data.map(sector => this.formBuilder.group(
          {
            hallId: [sector.hallId],
            sectorId: [sector.sectorId],
            name: [sector.name],
            standing: [sector.standing],
            price: [null, [Validators.required]]
          }
        ));
        console.log(format);
        console.log(this.eventForm.value);
        const performancesFormArray1 = this.eventForm.get('performances') as FormArray;
        const performanceFormGroup1 = performancesFormArray1.controls.find(performance => performance.value.hall === id) as FormGroup;
        performanceFormGroup1.setControl('sectorPrices', this.formBuilder.array(format));
        console.log(this.eventForm.value);
      }, error: error => {
        console.log(error);
        this.notification.error('Something went wrong');
      }
    });
  }
}
