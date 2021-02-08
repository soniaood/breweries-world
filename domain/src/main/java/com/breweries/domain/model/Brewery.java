package com.breweries.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representation of a Brewery.
 */
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brewery {
    Integer id;
    String name;
    String breweryType;
    String street;
    String address2;
    String address3;
    String city;
    String state;
    String countryProvince;
    String postalCode;
    String country;
    String longitude;
    String latitude;
    String phone;
    String websiteUrl;
    String updatedAt;
    String createdAt;
}
