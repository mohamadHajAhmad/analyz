package sdk.util;

import com.google.common.collect.Lists;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sdk.POJO.Fruit;
import sdk.POJO.Fruits;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestXml {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, JAXBException, JAXBException {
        System.out.print("test");
        Runtime rt = Runtime.getRuntime();
        long total_mem = rt.totalMemory();
        long free_mem = rt.freeMemory();
        long used_mem = total_mem - free_mem;
        System.out.println("Total memory: " + total_mem/1024/1024);
        System.out.println("Free memory: " + free_mem/1024/1024);
        System.out.println("Amount of used memory: " + used_mem/1024/1024);
        //File file=new File("fruit.xml");
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<fruits><employee id=\"0\"><n>Test</n><price>9.99</price></employee><employee id=\"1\"><n>Test</n><price>9.99</price></employee><employee id=\"2\"><n>Test</n><price>9.99</price></employee><employee id=\"3\"><n>Test</n><price>9.99</price></employee><employee id=\"4\"><n>Test</n><price>9.99</price></employee><employee id=\"5\"><n>Test</n><price>9.99</price></employee><employee id=\"6\"><n>Test</n><price>9.99</price></employee><employee id=\"7\"><n>Test</n><price>9.99</price></employee><employee id=\"8\"><n>Test</n><price>9.99</price></employee><employee id=\"9\"><n>Test</n><price>9.99</price></employee><employee id=\"10\"><n>Test</n><price>9.99</price></employee><employee id=\"11\"><n>Test</n><price>9.99</price></employee><employee id=\"12\"><n>Test</n><price>9.99</price></employee><employee id=\"13\"><n>Test</n><price>9.99</price></employee><employee id=\"14\"><n>Test</n><price>9.99</price></employee><employee id=\"15\"><n>Test</n><price>9.99</price></employee><employee id=\"16\"><n>Test</n><price>9.99</price></employee><employee id=\"17\"><n>Test</n><price>9.99</price></employee><employee id=\"18\"><n>Test</n><price>9.99</price></employee><employee id=\"19\"><n>Test</n><price>9.99</price></employee><employee id=\"20\"><n>Test</n><price>9.99</price></employee><employee id=\"21\"><n>Test</n><price>9.99</price></employee><employee id=\"22\"><n>Test</n><price>9.99</price></employee><employee id=\"23\"><n>Test</n><price>9.99</price></employee><employee id=\"24\"><n>Test</n><price>9.99</price></employee><employee id=\"25\"><n>Test</n><price>9.99</price></employee><employee id=\"26\"><n>Test</n><price>9.99</price></employee><employee id=\"27\"><n>Test</n><price>9.99</price></employee><employee id=\"28\"><n>Test</n><price>9.99</price></employee><employee id=\"29\"><n>Test</n><price>9.99</price></employee><employee id=\"30\"><n>Test</n><price>9.99</price></employee><employee id=\"31\"><n>Test</n><price>9.99</price></employee><employee id=\"32\"><n>Test</n><price>9.99</price></employee><employee id=\"33\"><n>Test</n><price>9.99</price></employee><employee id=\"34\"><n>Test</n><price>9.99</price></employee><employee id=\"35\"><n>Test</n><price>9.99</price></employee><employee id=\"36\"><n>Test</n><price>9.99</price></employee><employee id=\"37\"><n>Test</n><price>9.99</price></employee><employee id=\"38\"><n>Test</n><price>9.99</price></employee><employee id=\"39\"><n>Test</n><price>9.99</price></employee><employee id=\"40\"><n>Test</n><price>9.99</price></employee><employee id=\"41\"><n>Test</n><price>9.99</price></employee><employee id=\"42\"><n>Test</n><price>9.99</price></employee><employee id=\"43\"><n>Test</n><price>9.99</price></employee><employee id=\"44\"><n>Test</n><price>9.99</price></employee><employee id=\"45\"><n>Test</n><price>9.99</price></employee><employee id=\"46\"><n>Test</n><price>9.99</price></employee><employee id=\"47\"><n>Test</n><price>9.99</price></employee><employee id=\"48\"><n>Test</n><price>9.99</price></employee><employee id=\"49\"><n>Test</n><price>9.99</price></employee><employee id=\"50\"><n>Test</n><price>9.99</price></employee><employee id=\"51\"><n>Test</n><price>9.99</price></employee><employee id=\"52\"><n>Test</n><price>9.99</price></employee><employee id=\"53\"><n>Test</n><price>9.99</price></employee><employee id=\"54\"><n>Test</n><price>9.99</price></employee><employee id=\"55\"><n>Test</n><price>9.99</price></employee><employee id=\"56\"><n>Test</n><price>9.99</price></employee><employee id=\"57\"><n>Test</n><price>9.99</price></employee><employee id=\"58\"><n>Test</n><price>9.99</price></employee><employee id=\"59\"><n>Test</n><price>9.99</price></employee><employee id=\"60\"><n>Test</n><price>9.99</price></employee><employee id=\"61\"><n>Test</n><price>9.99</price></employee><employee id=\"62\"><n>Test</n><price>9.99</price></employee><employee id=\"63\"><n>Test</n><price>9.99</price></employee><employee id=\"64\"><n>Test</n><price>9.99</price></employee><employee id=\"65\"><n>Test</n><price>9.99</price></employee><employee id=\"66\"><n>Test</n><price>9.99</price></employee><employee id=\"67\"><n>Test</n><price>9.99</price></employee><employee id=\"68\"><n>Test</n><price>9.99</price></employee><employee id=\"69\"><n>Test</n><price>9.99</price></employee><employee id=\"70\"><n>Test</n><price>9.99</price></employee><employee id=\"71\"><n>Test</n><price>9.99</price></employee><employee id=\"72\"><n>Test</n><price>9.99</price></employee><employee id=\"73\"><n>Test</n><price>9.99</price></employee><employee id=\"74\"><n>Test</n><price>9.99</price></employee><employee id=\"75\"><n>Test</n><price>9.99</price></employee><employee id=\"76\"><n>Test</n><price>9.99</price></employee><employee id=\"77\"><n>Test</n><price>9.99</price></employee><employee id=\"78\"><n>Test</n><price>9.99</price></employee><employee id=\"79\"><n>Test</n><price>9.99</price></employee><employee id=\"80\"><n>Test</n><price>9.99</price></employee><employee id=\"81\"><n>Test</n><price>9.99</price></employee><employee id=\"82\"><n>Test</n><price>9.99</price></employee><employee id=\"83\"><n>Test</n><price>9.99</price></employee><employee id=\"84\"><n>Test</n><price>9.99</price></employee><employee id=\"85\"><n>Test</n><price>9.99</price></employee><employee id=\"86\"><n>Test</n><price>9.99</price></employee><employee id=\"87\"><n>Test</n><price>9.99</price></employee><employee id=\"88\"><n>Test</n><price>9.99</price></employee><employee id=\"89\"><n>Test</n><price>9.99</price></employee><employee id=\"90\"><n>Test</n><price>9.99</price></employee><employee id=\"91\"><n>Test</n><price>9.99</price></employee><employee id=\"92\"><n>Test</n><price>9.99</price></employee><employee id=\"93\"><n>Test</n><price>9.99</price></employee><employee id=\"94\"><n>Test</n><price>9.99</price></employee><employee id=\"95\"><n>Test</n><price>9.99</price></employee><employee id=\"96\"><n>Test</n><price>9.99</price></employee><employee id=\"97\"><n>Test</n><price>9.99</price></employee><employee id=\"98\"><n>Test</n><price>9.99</price></employee><employee id=\"99\"><n>Test</n><price>9.99</price></employee></fruits>\n";
        //System.out.print(XmlHelper.convertStringToXMLDocument(xml));
        Document document =XmlHelper.convertStringToXMLDocument(xml);
        System.out.println(document.getFirstChild().getNodeName());
        System.out.println(XmlHelper.convertDocumentToString(document));
        rt = Runtime.getRuntime();
        total_mem = rt.totalMemory();
        free_mem = rt.freeMemory();
        used_mem = total_mem - free_mem;
        System.out.println("Total memory: " + total_mem/1024/1024);
        System.out.println("Free memory: " + free_mem/1024/1024);
        System.out.println("Amount of used memory: " + used_mem/1024/1024);



    }

    public static boolean convertObjectToXMLList() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Lists.class, Fruit.class);
        try {

            jaxbContext = JAXBContext.newInstance(Fruits.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            // output to a xml file
            Fruits fruits=new Fruits();
            List<Fruit> fruitList=new ArrayList<>();
            for (int i=0;i<100;i++) {
                Fruit o = new Fruit();
                o.setId(i);
                o.setName("Test");
                o.setPrice("9.99");
                fruitList.add(o);
            }
            fruits.setFruit(fruitList);
            jaxbMarshaller.marshal(fruits, new File("fruit.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean convertXMLToObjList() throws JAXBException {
        // Create a JAXBContext and an Unmarshaller
        JAXBContext context = JAXBContext.newInstance(Fruits.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Unmarshal the XML file into an Example object
        Fruits example = (Fruits) unmarshaller.unmarshal(new File("fruit.xml"));

        // Print some properties of the Example object
        System.out.println(example.getFruit().size());
        return false;
    }
}
