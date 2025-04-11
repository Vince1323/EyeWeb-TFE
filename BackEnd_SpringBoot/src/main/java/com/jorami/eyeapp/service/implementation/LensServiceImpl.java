package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.Lens;
import com.jorami.eyeapp.model.LensManufacturer;
import com.jorami.eyeapp.repository.LensManufacturerRepository;
import com.jorami.eyeapp.repository.LensRepository;
import com.jorami.eyeapp.service.LensService;
import com.jorami.eyeapp.util.LensUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class LensServiceImpl implements LensService {

    private final LensRepository lensRepository;
    private final LensManufacturerRepository lensManufacturerRepository;


    @Override
    public List<Lens> getLensesByLensManufacturer(Long lensManufacturerId) {
        LensManufacturer lensManufacturer = lensManufacturerRepository.findLensManufacturerById(lensManufacturerId);
        return lensRepository.findLensesByLensManufacturer(lensManufacturer);
    }

    @Override
    public void importLensesFromWebSite() {
        //for each row:
        //position 0: name implant
        //position 1: comment trade name
        //position 2: nominal
        //position 3: Haigis (a0, a1, a2)
        //position 4: hofferQpACD
        //position 5: Holladay1
        //position 6: srk/t
        //position 7: Castrop (C,H,R)
        //position 8: name of manufacturer
        Lens lens;
        LensManufacturer lensManufacturer;
        List<List<String>> implants = LensUtils.getAllImplants();
        String haigisFormatOptimized;
        String CastropFormatOptimized;
        String haigisFormat;
        String name = "";
        String commentTradeName = "";
        String nominal = "";
        String haigisA0Optimized;
        String haigisA1Optimized;
        String haigisA2Optimized;
        String hofferQpACDOptimized;
        String holladay1Optimized;
        String srkTOptimized;
        String castropCOptimized;
        String castropHOptimized;
        String castropROptimized;
        String cookeOptimized;
        String nameManufacturer="";
        String haigisA0;
        String haigisA1;
        String haigisA2;
        String hofferQpACD;
        String holladay1;
        String srkT;
        long id = 0;

        for (int i = 0; i < implants.size(); i++) {
            lens = new Lens();
            haigisA0Optimized = null;
            haigisA1Optimized= null;
            haigisA2Optimized= null;
            hofferQpACDOptimized= null;
            holladay1Optimized= null;
            srkTOptimized= null;
            castropCOptimized= null;
            castropHOptimized= null;
            castropROptimized= null;
            cookeOptimized= null;
            haigisA0= null;
            haigisA1= null;
            haigisA2= null;
            hofferQpACD= null;
            holladay1= null;
            srkT= null;
            nominal = null;
            name = implants.get(i).get(0);
            commentTradeName =implants.get(i).get(1);
            nominal = implants.get(i).get(2);
            haigisFormatOptimized = LensUtils.formatNumbers(implants.get(i).get(3));
            CastropFormatOptimized = LensUtils.formatNumbers(implants.get(i).get(7));
            haigisFormat = LensUtils.formatNumbers(implants.get(i).get(9));
            if(LensUtils.splittedValues(haigisFormatOptimized).size()>0) {
                haigisA0Optimized = LensUtils.splittedValues(haigisFormatOptimized).get(0);
                haigisA1Optimized = LensUtils.splittedValues(haigisFormatOptimized).get(1);
                haigisA2Optimized = LensUtils.splittedValues(haigisFormatOptimized).get(2);
            }
            if(implants.get(i).get(4).length()>0)
                hofferQpACDOptimized = implants.get(i).get(4);
            if(implants.get(i).get(5).length()>0)
                holladay1Optimized = implants.get(i).get(5);
            if(implants.get(i).get(6).length()>0)
                srkTOptimized = implants.get(i).get(6);
            if(LensUtils.splittedValues(CastropFormatOptimized).size()>0) {
                castropCOptimized = LensUtils.splittedValues(CastropFormatOptimized).get(0);
                castropHOptimized = LensUtils.splittedValues(CastropFormatOptimized).get(1);
                castropROptimized = LensUtils.splittedValues(CastropFormatOptimized).get(2);
            }
            if(LensUtils.splittedValues(haigisFormat).size()>0) {
                haigisA0 = LensUtils.splittedValues(haigisFormat).get(0);
                haigisA1 = LensUtils.splittedValues(haigisFormat).get(1);
                haigisA2 = LensUtils.splittedValues(haigisFormat).get(2);
            }
            if(implants.get(i).get(10).length()>0)
                hofferQpACD = implants.get(i).get(10);
            if(implants.get(i).get(11).length()>0)
                holladay1 = implants.get(i).get(11);
            if(implants.get(i).get(12).length()>0)
                srkT = implants.get(i).get(12);
            if(implants.get(i).get(13).length()>0)
                cookeOptimized = implants.get(i).get(13);
            nameManufacturer = implants.get(i).get(8);

            lensManufacturer = lensManufacturerRepository.findLensManufacturerByName(nameManufacturer);
            if(lensManufacturer == null) {
                lensManufacturer = new LensManufacturer();
                lensManufacturer.setName(nameManufacturer);
                lensManufacturerRepository.save(lensManufacturer);
            }
            lens.setName(name);
            lens.setCommentTradeName(commentTradeName);
            lens.setNominal(LensUtils.parseFloat(nominal));
            lens.setCookeOptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(cookeOptimized)));
            lens.setCastropCOptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(castropCOptimized)));
            lens.setCastropHOptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(castropHOptimized)));
            lens.setCastropROptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(castropROptimized)));
            lens.setHaigisA0Optimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(haigisA0Optimized)));
            lens.setHaigisA1Optimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(haigisA1Optimized)));
            lens.setHaigisA2Optimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(haigisA2Optimized)));
            lens.setSrktaOptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(srkTOptimized)));
            lens.setHofferQPACDOptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(hofferQpACDOptimized)));
            lens.setHolladay1SFOptimized(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(holladay1Optimized)));
            lens.setHaigisA0(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(haigisA0)));
            lens.setHaigisA1(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(haigisA1)));
            lens.setHaigisA2(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(haigisA2)));
            lens.setSrkta(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(srkT)));
            lens.setHofferQPACD(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(hofferQpACD)));
            lens.setHolladay1SF(LensUtils.parseFloat(LensUtils.verifyAndRemoveLast(holladay1)));
            lens.setLensManufacturer(lensManufacturer);


            if(!lensRepository.lensAlreadyExists(lens.getName(), lens.getCommentTradeName())) {
                lensRepository.save(lens);
            }
            else {
                id = lensRepository.findLensIdByNameAndComment(lens.getName(),lens.getCommentTradeName());
                lens.setId(id);
                lensRepository.save(lens);
            }
        }
    }

}
