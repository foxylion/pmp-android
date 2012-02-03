package de.unistuttgart.ipvs.pmp.jpmpps.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.unistuttgart.ipvs.pmp.jpmpps.JPMPPS;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;

public class ResponseHashChecker {

	public static boolean checkHash(String locale,
			LocalizedResourceGroup[] rgs, byte[] hash) {
		StringBuilder hashBuilder = new StringBuilder();
		hashBuilder.append(locale);
		for (LocalizedResourceGroup rg : rgs) {
			hashBuilder.append(rg.getIdentifier());
		}

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(hashBuilder.toString().getBytes(), 0, hashBuilder
					.toString().length());
			return new String(md.digest()).equals(new String(hash));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Do not know the MD5-Hashing alogrithm.");

			if (JPMPPS.DEBUG) {
				e.printStackTrace();
			}
		}

		return false;
	}
}
