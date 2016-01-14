import java.security.*;
import java.io.*;

public class ComputeSHA{

	public static String get_hash(String file) throws Exception{
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[(int)file.length()];

		MessageDigest md = MessageDigest.getInstance("SHA-1");

		int nread = 0;
		while((nread = fis.read(buffer)) != -1){
			md.update(buffer, 0, nread);
		};
		fis.close();
		byte[] hash = md.digest();

		StringBuilder retval = new StringBuilder();
		for(byte i : hash){
			retval.append(Integer.toString((0xFF & i) + 0x100, 16).substring(1));
		}

		return retval.toString();
	}

	public static void main(String[] args){
		if(args.length > 0){
			try{
				String hash = get_hash(args[0]);
				System.out.println(hash);
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
		}else{
			System.out.println("no file provided!");
		}
	}
}
