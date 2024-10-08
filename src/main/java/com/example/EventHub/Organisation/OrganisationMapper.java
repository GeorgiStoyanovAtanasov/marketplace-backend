package com.example.EventHub.Organisation;

import org.springframework.stereotype.Component;

@Component
public class OrganisationMapper {
    public OrganisationDTO toDTO(Organisation organisation) {
        if (organisation == null) {
            return null;
        }
        return new OrganisationDTO(organisation.getId(), organisation.getName(), organisation.getOrganisationPermission());
    }

    public Organisation toEntity(OrganisationDTO organisationDTO) {
//        if (organisationDTO == null) {
//            return null;
//        }
        Organisation organisation = new Organisation();
        organisation.setId(organisationDTO.getId());
        organisation.setName(organisationDTO.getName());
        organisation.setOrganisationPermission(organisation.getOrganisationPermission());
        return organisation;
    }
}

