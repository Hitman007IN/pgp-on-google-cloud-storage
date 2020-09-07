package com.example.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;

public interface PGPManager {

	public void encryptFile(String originalFile, InputStream keyFile, OutputStream encryptFile,
			boolean asciiArmored, boolean integrityCheck) throws NoSuchProviderException, IOException, PGPException;

	public void decryptFile(InputStream encryptFile, OutputStream dencryptFile, InputStream keyFile, String passphrase)
			throws NoSuchProviderException, IOException, PGPException;

}
