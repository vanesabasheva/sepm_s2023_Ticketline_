package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Collections;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.AddressApi;
import com.cloudmersive.client.model.NormalizeAddressResponse;
import com.cloudmersive.client.model.ValidateAddressRequest;

public class LocationDto {
    private Integer postalCode;
    private String city;
    private String country;
    private String street;


    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (postalCode == null || postalCode.toString().isBlank() || city == null || city.isBlank()
            || country == null || country.isBlank() || street == null || street.isBlank()) {
            return List.of("All fields are required");
        }

        //regex for postal code
        final String postalCodeRegex = "^[0-9]{4,5}$";
        //regex for city
        final String cityRegex = "^[a-zA-ZäöüÄÖÜß]+(([',. -][a-zA-ZäöüÄÖÜß\\s])?[a-zA-ZäöüÄÖÜß]*)*$";

        //regex for street
        final String streetRegex = "^(?=.*[a-zA-ZäöüÄÖÜß])[a-zA-Z0-9äöüÄÖÜß\\s-.,#]+$";

        if (!postalCode.toString().matches(postalCodeRegex)) {
            errors.add("Invalid postal code format. Postal code must be 4 or 5 digits long");
        }
        if (!city.matches(cityRegex)) {
            errors.add("Invalid city format. City must be at least 2 characters long and contain only letters");
        }
        //County must be valid ISO country code. e.g. AT, DE, US
        if (!Set.of(Locale.getISOCountries()).contains(country)) {
            errors.add("Invalid country format. Country must be a valid ISO country code");
        }
        if (!street.matches(streetRegex)) {
            errors.add("Invalid street format.");
        }
        return errors;
    }

    public void validateApi() throws ValidationException, ConflictException {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        apiKey.setApiKey("d7eba15e-71e5-470e-af71-2862e95a5c1c");

        AddressApi apiInstance = new AddressApi();
        ValidateAddressRequest input = new ValidateAddressRequest(); // ValidateAddressRequest | Input parse request
        input.streetAddress(this.street);
        input.city(this.city);
        input.stateOrProvince("");
        input.postalCode(this.postalCode.toString());
        input.countryFullName("");
        input.countryCode(this.country);
        NormalizeAddressResponse result = null;
        try {
            result = apiInstance.addressNormalizeAddress(input);
            System.out.println(result);
        } catch (ApiException e) {
            throw new ConflictException("Cloudmersive API returned an error", Collections.singletonList(e.getMessage()));
        }
        if (result == null) {
            throw new ConflictException("API did not return anything", Collections.singletonList("Are Cloudmersive servers online?"));
        }
        if (!result.isValidAddress()) {
            throw new ValidationException("Address is not valid", Collections.singletonList("Please check your input"));
        }
    }

    public static final class LocationDtoBuilder {
        private Integer postalCode;
        private String city;
        private String country;
        private String street;

        private LocationDtoBuilder() {
        }

        public static LocationDtoBuilder aLocationDto() {
            return new LocationDtoBuilder();
        }

        public LocationDtoBuilder withPostalCode(Integer postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public LocationDtoBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public LocationDtoBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public LocationDtoBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public LocationDto build() {
            LocationDto locationDto = new LocationDto();
            locationDto.setPostalCode(postalCode);
            locationDto.setCity(city);
            locationDto.setCountry(country);
            locationDto.setStreet(street);
            return locationDto;
        }
    }
}
