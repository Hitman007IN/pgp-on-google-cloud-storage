package com.example.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.storage.GoogleStorageResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.configurations.PGPConfigProperties;
import com.example.manager.PGPManager;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@RestController
public class PGPController {

	private GoogleStorageResource gcsResource;

	@Autowired
	PGPManager pgpManager;

	@Autowired
	PGPConfigProperties pgpProps;
	
	@GetMapping("/enc")
	public void encrypt(@RequestHeader(value="plain", required=true) String plainText, 
			@RequestHeader(value="cipher", required=true) String encText) throws NoSuchProviderException, IOException, PGPException {
		
		Storage storage =  StorageOptions.newBuilder().setProjectId("feisty-reef-282205").build().getService();
		gcsResource = new GoogleStorageResource(storage, encText, true);
		
		InputStream is = new FileInputStream(pgpProps.getPublicKeyFilePath());

		gcsResource.createBlob();

		pgpManager.encryptFile(plainText, is, gcsResource.getOutputStream(),
				pgpProps.isAsciiArmored(), pgpProps.isIntegrityCheck());
	}

	@GetMapping("/dec")
	public void decrypt(@RequestHeader(value="cipher", required=true) String encText, 
			@RequestHeader(value="plain", required=true) String decText) throws NoSuchProviderException, IOException, PGPException {

		Storage storage =  StorageOptions.newBuilder().setProjectId("feisty-reef-282205").build().getService();
		gcsResource = new GoogleStorageResource(storage, encText, true);
		
		OutputStream os = new FileOutputStream(decText);
		InputStream is = new FileInputStream(pgpProps.getSecretKeyFilePath());

		pgpManager.decryptFile(gcsResource.getInputStream(), os, is, pgpProps.getPassphrase());
	}
	
	
}
