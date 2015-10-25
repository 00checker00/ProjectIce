package de.project.ice.spriteconverter;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SpriteConverter
{
    public static void main(String[] argv)
    {
        if (argv.length != 1)
        {
            System.err.println("Usage:   java de.project.ice.spriteconverter.SpriteConverter <XmlFile>");
            System.err.println("Example: java ExampleDomShowNodes MyXmlFile.xml");
            System.exit(1);
        }
        try
        {
            // ---- Parse XML file ----
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(argv[0]));

            Node textureAtlasNode = document.getElementsByTagName("TextureAtlas").item(0);
            NamedNodeMap textureAtlasAttributes = textureAtlasNode.getAttributes();
            String imagePath = textureAtlasAttributes.getNamedItem("imagePath").getNodeValue();

            NodeList ndList = document.getElementsByTagName("SubTexture");


            ArrayList<Texture> textures = new ArrayList<Texture>(ndList.getLength());

            for (int i = 0; i < ndList.getLength(); i++)
            {
                Node node = ndList.item(i);
                NamedNodeMap attributes = node.getAttributes();
                Texture texture = new Texture();
                String name = attributes.getNamedItem("name").getNodeValue();
                texture.name = name.substring(0, name.length() - 4);
                texture.index = Integer.parseInt(name.substring(name.length() - 4));
                texture.x = Integer.parseInt(attributes.getNamedItem("x").getNodeValue());
                texture.y = Integer.parseInt(attributes.getNamedItem("y").getNodeValue());
                texture.width = Integer.parseInt(attributes.getNamedItem("width").getNodeValue());
                texture.height = Integer.parseInt(attributes.getNamedItem("height").getNodeValue());
                if (attributes.getNamedItem("frameX") != null)
                {
                    texture.offsetX = Integer.parseInt(attributes.getNamedItem("frameX").getNodeValue());
                }
                if (attributes.getNamedItem("frameY") != null)
                {
                    texture.offsetX = Integer.parseInt(attributes.getNamedItem("frameY").getNodeValue());
                }

                if (attributes.getNamedItem("frameWidth") != null)
                {
                    texture.origWidth = Integer.parseInt(attributes.getNamedItem("frameWidth").getNodeValue());
                }
                else
                {
                    texture.origWidth = texture.width;
                }

                if (attributes.getNamedItem("frameHeight") != null)
                {
                    texture.origHeight = Integer.parseInt(attributes.getNamedItem("frameHeight").getNodeValue());
                }
                else
                {
                    texture.origHeight = texture.height;
                }
                textures.add(texture);

            }

            FileWriter fw = new FileWriter(argv[0].replace(".xml", "") + ".atlas");
            fw.write(imagePath + "\n" +
                    "format: RGBA8888\n" +
                    "filter: Linear,Linear\n" +
                    "repeat: none\n");
            for (Texture texture : textures)
            {
                fw.write(texture.name);
                fw.write("\n");
                fw.write("  rotate: ");
                fw.write(texture.rotate ? "true" : "false");
                fw.write("\n");
                fw.write("  xy: ");
                fw.write(String.valueOf(texture.x));
                fw.write(" ,");
                fw.write(String.valueOf(texture.y));
                fw.write("\n");
                fw.write("  size: ");
                fw.write(String.valueOf(texture.width));
                fw.write(" ,");
                fw.write(String.valueOf(texture.height));
                fw.write("\n");
                fw.write("  orig: ");
                fw.write(String.valueOf(texture.origWidth));
                fw.write(" ,");
                fw.write(String.valueOf(texture.origHeight));
                fw.write("\n");
                fw.write("  offset: ");
                fw.write(String.valueOf(texture.offsetX));
                fw.write(" ,");
                fw.write(String.valueOf(texture.offsetY));
                fw.write("\n");
                fw.write("  index:");
                fw.write(String.valueOf(texture.index));
                fw.write("\n");
            }
            fw.close();

            // ---- Error handling ----
        }
        catch (SAXParseException spe)
        {
            System.out.println("\n** Parsing error, line " + spe.getLineNumber()
                    + ", uri " + spe.getSystemId());
            System.out.println("   " + spe.getMessage());
            Exception e = (spe.getException() != null) ? spe.getException() : spe;
            e.printStackTrace();
        }
        catch (SAXException sxe)
        {
            Exception e = (sxe.getException() != null) ? sxe.getException() : sxe;
            e.printStackTrace();
        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private static class Texture
    {
        public String name = "";
        public boolean rotate = false;
        public int x = 0;
        public int y = 0;
        public int width = 0;
        public int height = 0;
        public int origWidth = 0;
        public int origHeight = 0;
        public int offsetX = 0;
        public int offsetY = 0;
        public int index = 1;
    }
}
