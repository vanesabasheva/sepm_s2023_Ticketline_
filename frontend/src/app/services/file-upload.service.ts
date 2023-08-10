import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {FilePath} from '../dtos/createEvent/filePath';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileUploadService {

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }


  uploadImage(image: File): Observable<FilePath> {
    const formData = new FormData();
    formData.append('image', image);
    return this.httpClient.post<FilePath>(this.globals.backendUri + '/image', formData);
  }
}
