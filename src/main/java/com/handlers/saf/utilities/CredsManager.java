package com.handlers.saf.utilities;

import org.springframework.stereotype.Service;

import com.handlers.saf.models.CredentialsObject;

@Service
public class CredsManager {

	public CredentialsObject getCreds(String shortCode) {

		// Probably get this from a database & decrypt
		CredentialsObject obj = new CredentialsObject();

		obj.setConsumerKey("vVkZstoGU3NGjrIbSnk8IIMejUTGNzf32LxDiZGcJKn9HnhI");
		obj.setConsumerSecret("AakFNIRrOvtpn0nAhxKHMRnvcYVSU066XkRE670t2ISXG2Al9XVF841JUSbZSn8B");
		obj.setPassKey("bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919");
		
		
		obj.setInitiatorName("testapi");
		obj.setSecurityCredential("MJXlQ9fvSFm5AG75G/kBPGbNbVlTxHb4lKjl8JYpth6Wwf5ag7Y002snhSrRReeQYtBgS1UPmLmGMJcbyAMWHzYZ/wsR2o6ld6Ez5S2CypZEaUKUPMSujvQQwngG3P68ps/16afgkzuRn33GynCieV8cfWH98zMgRo6gwoR8FU1tn0Cvmmh0TK3Vn/OcKEQLM1+Nidm55M1IbqQmsiwTKl85tT3G+zmbpRiUgfJaUkaSFGstFPI9erftpP/Aqc0JAtYHr4GnJbzY09WUX5xRhfoVDQHr3IrKYDjgRxHV1b8X/X2DZ6JoHbvnCFfe89nHaQkPt4HatcSWVC3xkIrt8A==");return obj;
	}

}
