import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class WriteTestFile
{
    // max numbers of inproceedings
    private final static int BORDER = 1900;

    private static FileOutputStream output;

    static {
        try {
            output = new FileOutputStream("dblp_test_file.xml.gz");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Writer writer;

    static {
        try {
            writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    };


    private static void readXMLFile(String filename) throws IOException
    {
        GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(filename));
        BufferedReader reader = new BufferedReader(new InputStreamReader(gzip));

        // The beginning of the new test file
        String result = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                        "<!DOCTYPE dblp SYSTEM \"dblp.dtd\">\n" +
                         "<dblp>\n";

        writeFile(result);

        String line = "";
        int count = 0;
        while((line = reader.readLine()) != null)
        {

            if (count < 0 && line.startsWith("<inproceedings"))
            {
                System.out.println(line + "\t" + count);
                writeFile(line + " \n");
                while (!(line = reader.readLine()).contains("</inproceedings>"))
                {

                    writeFile(line + " \n");
                }
                writeFile(line + " \n");
                count++;


            }

            if ( line.contains("key=\"homepages/"))
            {
                writeFile(line + " \n");
                while (!(line = reader.readLine()).contains("</www>"))
                {
                    writeFile(line + " \n");
                }
                writeFile(line + " \n");
            }
            //countPers++;

//            if (countPers < 10 && count < 10)
//            {
//                break;
//            }
        }


        writeFile("</dblp>" + " \n");
    }


    private static void writeFile(String content) throws IOException
    {
        writer.write(content);

    }

    public static void main(String[] args)
    {
        try {
            readXMLFile("dblp.xml.gz");
            System.out.println("Read the complete file.");
            System.out.println("Wrote the complete file.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
