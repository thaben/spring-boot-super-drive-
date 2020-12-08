package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.entities.Credential;
import com.udacity.jwdnd.course1.cloudstorage.repository.CredentialsRepository;

@Service
public class CredentialService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialService.class);

    private CredentialsRepository credentialsRepository;

    private EncryptionService encryptionService;

    CredentialService() {
    }

    @Autowired
    public CredentialService(CredentialsRepository credentialsRepository, EncryptionService encryptionService) {
        this.credentialsRepository = credentialsRepository;
        this.encryptionService = encryptionService;
    }

    public Boolean deleteCredential(Integer credentialId, Integer userId) {
        try {
            int rowsAffected = credentialsRepository.deleteByCredentialIdAndUserId(credentialId, userId);
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.error("Error while deleting credential with credential id {}", credentialId);
            throw e;
        }

    }

    public Boolean saveOrUpdateCredential(Credential credential) {
        String key = encryptionService.createKey();
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), key);
        credential.setPassword(encryptedPassword);
        credential.setKey(key);

        if (credential.getCredentialid() == null) {
            return saveCredential(credential);
        } else {
            return updateCredentials(credential);
        }
    }


    public Boolean updateCredentials(Credential credential) {
        LOGGER.info("Credentials that are going to be updated {}", credential);
        try {
            return credentialsRepository.update(credential) > 0;
        } catch (Exception e) {
            LOGGER.error("Error while updating credentials {}", credential);
            e.printStackTrace();
            return false;
        }

    }

    public Boolean saveCredential(Credential credential) {
        LOGGER.info("Credentials that are going to be saved {}", credential);
        try {
            Boolean isSaved = credentialsRepository.save(credential) > 0;
            LOGGER.info("Credential id generated {}", credential.getCredentialid());
            return isSaved;
        } catch (Exception e) {
            LOGGER.error("Error while saving credentials {}", credential);
            e.printStackTrace();
            return false;
        }

    }

    public List<Credential> findAllCredentials(Integer userId) {
        try {
            List<Credential> credentials = credentialsRepository.findByUserId(userId);
            credentials.forEach(cr -> cr.setUnencryptedpassword(encryptionService.decryptValue(cr.getPassword(), cr.getKey())));
            LOGGER.info("Credentials found for user id {}, : {}", userId, credentials);
            return credentials;
        } catch (Exception e) {
            LOGGER.error("Error while finding all credentials for userId {}", userId);
            throw e;
        }

    }

}