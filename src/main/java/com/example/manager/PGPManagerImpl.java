package com.example.manager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;

import org.bouncycastle.openpgp.PGPException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.utils.PGPUtils;

@Service
public class PGPManagerImpl implements PGPManager {

	private static Logger logger = LoggerFactory.getLogger(PGPManagerImpl.class);

	@Override
	public void encryptFile(String originalFile, InputStream keyFile, OutputStream encryptFile,
			boolean asciiArmored, boolean integrityCheck) throws NoSuchProviderException, IOException, PGPException {

		logger.debug("encryptFile()");

		PGPUtils.encryptFile(encryptFile, originalFile, PGPUtils.readPublicKey(keyFile), asciiArmored, integrityCheck);

		keyFile.close();
		encryptFile.close();

	}

	@Override
	public void decryptFile(InputStream encryptFile, OutputStream decryptFile, InputStream keyFile,
			String passphrase) throws NoSuchProviderException, IOException, PGPException {

		logger.debug("decryptFile()");

		PGPUtils.decryptFile(encryptFile, decryptFile, keyFile, passphrase.toCharArray());

		encryptFile.close();
		decryptFile.close();
		keyFile.close();

	}

}
