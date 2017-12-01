package com.swacorp.ais.cdsSalesEventFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

import com.embassy.file.EmbassyFileList;
import com.embassy.file.EmbassyFileReader;
import com.embassy.file.EmbassySearchCriteria;

@Controller
@Configuration
public class CdsSalesEventFilterController {
   @Autowired
   private Environment _environment;

   Logger _logger = LogManager.getLogger();

   public void process(String[] args) throws IOException, TransformerFactoryConfigurationError, SAXException,
      ParserConfigurationException, TransformerException, XPathExpressionException {
      _logger.error("Started {}", getClass().getSimpleName());
      
      EmbassySearchCriteria embassySearchCriteria = new EmbassySearchCriteria();
      embassySearchCriteria.addSourceDirectory(_environment.getProperty("source"));
      embassySearchCriteria.addSourceDirectory("c:/data/einvoicing/testdata");
      embassySearchCriteria.addIncludeSuffix(".xml");
      embassySearchCriteria.addIncludeSuffix(".txt");
      embassySearchCriteria.addWildcardFileSpec("*SaleEmdExchanged*");
      embassySearchCriteria.setRecurseDirectories(true);
      
      EmbassyFileList embassyFileList = new EmbassyFileList();
      Collection<File> fileList = embassyFileList.listFiles(embassySearchCriteria);
      _logger.info("Found {} files", fileList.size());
      System.out.println("Found " + fileList.size() + " files");
      
      for (File file : fileList) {
         _logger.info("File = {}", file);
      }
//      List<File> files = getFileList();

//      for (File file : files) {
//         processFile(file);
//      }
   }

   private void processFile(File file) throws FileNotFoundException, IOException, XPathExpressionException, SAXException, ParserConfigurationException {
         EmbassyFileReader fileReader = new EmbassyFileReader(file);
         String line = null;
         String document = new String();

         while ((line = fileReader.getRecord()) != null) {
            document = document.concat(line);
         }
         
         processRecord(document, file);
         fileReader.close();

   }

   private void processRecord(String record, File file) throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException, XPathExpressionException {
      CdsSalesEventMessage cdsSalesEventMessage = new CdsSalesEventMessage(record);
      String eventType = cdsSalesEventMessage.getEventType();
      String ticketNumber = cdsSalesEventMessage.getTicketNumber();
      String recordLocatorId = cdsSalesEventMessage.getRecordLocatorId();
      String deploymentActivity = cdsSalesEventMessage.getDeploymentActivity();
      int xoTaxCount = cdsSalesEventMessage.getTax();
      
      if ((xoTaxCount > 0) && (!deploymentActivity.equals("PPV")) && (!deploymentActivity.equals("Migration"))) {
         _logger.info("{}, {}, {}, {}, {}", eventType, ticketNumber, recordLocatorId, xoTaxCount, file.getName());
      }
   }
}
