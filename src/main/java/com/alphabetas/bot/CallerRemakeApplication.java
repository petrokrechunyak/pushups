package com.alphabetas.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@SpringBootApplication
public class CallerRemakeApplication {

    public static void main(String[] args) {

        String[] arr = ("AP98\n" +
                "AP01\n" +
                "AP09A\n" +
                "ZAP2A\n" +
                "AP81\n" +
                "ZAP9SB\n" +
                "AV7BR\n" +
                "AV2A1\n" +
                "AV1PR\n" +
                "BA9\n" +
                "BA1\n" +
                "BE2B2\n" +
                "BE1D\n" +
                "BE3A\n" +
                "CAB9B1\n" +
                "CABH10\n" +
                "ZCAB1LM\n" +
                "CAR994B\n" +
                "CAR5\n" +
                "CAR96E1\n" +
                "CAU2B\n" +
                "FLORENTINOAB\n" +
                "CAULILINI\n" +
                "CE4A1\n" +
                "CE10AB\n" +
                "ZCE6\n" +
                "SFCODF01\n" +
                "SFCODF04\n" +
                "SFHALF02\n" +
                "SFHALP03\n" +
                "SEAL2\n" +
                "SEAL3\n" +
                "SEAL5\n" +
                "SFSALF02\n" +
                "SFSALP52\n" +
                "MEBNGF01\n" +
                "MEPO1JF1A01\n" +
                "MEDUC301\n" +
                "MEDUC1B02\n" +
                "MEDUC101\n" +
                "MESMN4MH01\n" +
                "MEGAM4A01\n" +
                "MEPKC31401\n" +
                "MEPKN201\n" +
                "MELA26B01\n" +
                "MELA17M02\n" +
                "MEBNGFP502\n" +
                "PABUTAR01\n" +
                "PABUT102\n" +
                "DACREAM1\n" +
                "DAICECREAMS2\n" +
                "EGGS9E\n" +
                "EGGS9A\n" +
                "EGGS9\n" +
                "PAMILKO4CS\n" +
                "PAMILKRB1\n" +
                "DAMILK3AA\n" +
                "PAYOGURT4\n" +
                "PACHEESE7\n" +
                "PACHEESE16G\n" +
                "PACHEESE11C\n" +
                "CHEESEN3\n" +
                "CHEESE6B\n" +
                "PACHEESE3D\n" +
                "PACHEESE4J\n" +
                "MC6668\n" +
                "DACHEESE5FF\n" +
                "DACHEESE5FO\n" +
                "MCH109\n" +
                "PACHEESE4K\n" +
                "MC6705").split("\n");

        StringBuilder result = new StringBuilder("INSERT_UPDATE Media;mediaFormat(qualifier);code[unique=true];@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator];mime[default='image/jpg'];$catalogVersion;folder(qualifier)\n");

        for(String s: arr) {
            result.append("; 1200Wx1200H; /515Wx515H/").append(s).append(".jpg; $siteResource/images").append("/515Wx515H/").append(s).append(".jpg;;;images\n");
        }

        System.out.println(result);

        SpringApplication.run(CallerRemakeApplication.class, args);
    }

}
