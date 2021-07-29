import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class Parser {


    private int anonymousPublications = 0;
    private List<Publication> publications = new ArrayList<Publication>();
    private List<Conferences_Journals> conferences_journalsList = new ArrayList<>();
    private List<String> confJournalsNames = new ArrayList<>();

    private boolean newElementInList = false;
	
    private class ConfigHandler extends DefaultHandler {
        
        private String Value;
        private String key;
        private String ee;
        private boolean insideName = false;
        private boolean insideEE = false;
        private boolean insidePersonRecord = false;
        private boolean insidePublicationConf = false;
        private boolean insidePublicationJournals = false;
        private boolean journalsCorr = false;
        private int level = 0;
        private List<PersonName> names = new ArrayList<PersonName>();


        public void setDocumentLocator(Locator locator) {
        }

        public void startElement(String namespaceURI, String localName,
                String rawName, Attributes atts) throws SAXException {
        	level++;
        	if (level == 2) {
        		key = atts.getValue("key");

                // Für die Personen:
        		insidePersonRecord = key.startsWith("homepages/");
        		ee = null;

                // Für die conf/
                insidePublicationConf = key.startsWith("conf/");

                // Für die journals/
                insidePublicationJournals = key.startsWith("journals/");

                // Für die journals/corr/
                journalsCorr = key.startsWith("journals/corr/");
        		return;
        	}
        	
        	if (rawName.equals("author") || rawName.equals("editor")) {
        		insideName = true;
        		Value = "";
        	}


        	if (rawName.equals("ee")) {
        		insideEE = true;
        		Value = "";
        	}
        }

        public void endElement(String namespaceURI, String localName,
                String rawName) throws SAXException {
        	level--;
        	
        	if (rawName.equals("author") || rawName.equals("editor")) {
        		insideName = false;
        		names.add(PersonName.createName(Value));
        		return;
        	}
        	if (rawName.equals("ee")) {
        		insideEE = false;
        		if (ee == null && Value.startsWith("https://doi.org/")) {
        			ee = Value.substring(16);
        		}
        		return;
        	}
            
        	if (level != 1) 
        		return;
        	if (names.size()==0) {
        		anonymousPublications++;
        		return;
        	}

        	PersonName pn[] = names.toArray(new PersonName[names.size()]);
        	names.clear();
        	if (insidePersonRecord)
        		new Person(pn);
        	// ignore
        	else if (journalsCorr)
        	    return;
        	else if (insidePublicationConf || insidePublicationJournals)
        		new Publication(key, pn, ee);
        }

        // Baue den Namen zusammen
        public void characters(char[] ch, int start, int length)
                throws SAXException {
        	if (insideName || insideEE)
             	Value += new String(ch, start, length);
        }




        // Standard Methoden
        private void Message(String mode, SAXParseException exception) {
            System.out.println(mode + " Line: " + exception.getLineNumber()
                    + " URI: " + exception.getSystemId() + "\n" + " Message: "
                    + exception.getMessage());
        }

        public void warning(SAXParseException exception) throws SAXException {

            Message("**Parsing Warning**\n", exception);
            throw new SAXException("Warning encountered");
        }

        public void error(SAXParseException exception) throws SAXException {

            Message("**Parsing Error**\n", exception);
            throw new SAXException("Error encountered");
        }

        public void fatalError(SAXParseException exception) throws SAXException {

            Message("**Parsing Fatal Error**\n", exception);
            throw new SAXException("Fatal Error encountered");
        }
    }

    Parser(String uri) {
        
        try {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            ConfigHandler handler = new ConfigHandler();
            parser.getXMLReader().setFeature(
                    "http://xml.org/sax/features/validation", true);
            if (uri.endsWith(".gz"))
				parser.parse(new GZIPInputStream(new FileInputStream(uri)), handler);
			else
				parser.parse(new File(uri), handler);
        } catch (IOException e) {
            System.out.println("Error reading URI: " + e.getMessage());
        } catch (SAXException e) {
            System.out.println("Error in parsing: " + e.getMessage());
        } catch (ParserConfigurationException e) {
            System.out.println("Error in XML parser configuration: "
                    + e.getMessage());
        }



        // Output:
        System.out.println(anonymousPublications + "  anonymous publications (ignored)");
        System.out.println(Publication.size() + "  publications");
        System.out.println(PersonName.size() + "  person names");
        System.out.println(Person.size() + "  persons");

        Person.buildPersonPublicationEdges();

//        // Alle gefundenen Publikationen:
//        for (Publication p: this.publications)
//        {
//
//            //p.getContributorsIDs();
//
//            // Entnehme den Präfix der Publikation p
//            int prefixIndex = p.getKey().lastIndexOf("/");
//
//
//            conferences_journalsList.stream().forEach((cJ) ->
//            {
//                //System.out.println();
//                if(p.getKey().substring(0, prefixIndex).equals(cJ.getSubKeys()))
//                {
//                    //cJ.getListContributors().addAll(Arrays.asList(p.getContributorsArray()));
//                    Arrays.asList(p.getContributorsArray()).forEach(personName -> {
//                        cJ.getListContributors().put(p.getKey().substring(0, prefixIndex), (Set<Person>) personName.getId());
//                        System.out.println(personName.getId());
//
//                    });
//                    newElementInList = true;
//                    return;
//                }
//            });
//
//            if (!newElementInList)
//            {
//                conferences_journalsList.add(new Conferences_Journals(p));
//            }
//
//            newElementInList = false;
//
//        }
//
//        //System.out.println(conferences_journalsList.size());
//        for (Conferences_Journals cjObject: conferences_journalsList)
//        {
//            System.out.println(cjObject);
//        }
    }
}
