package de.project.ice.spriteconverter;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.Json;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Packer
{
    public static void main(String[] args)
    {
        File dir;
        File outdir;

        if (args.length == 0) {
            dir = new File(".").getAbsoluteFile();
            outdir = new File(dir, "output");
        } else if (args.length == 1) {
            dir = new File(args[0]);
            if (!dir.exists()) {
                System.out.println("Input dir does not exist");
                System.exit(1);
            }
            dir = dir.getAbsoluteFile();
            outdir = new File(dir, "output");
        } else if (args.length == 2) {
            dir = new File(args[0]);
            if (!dir.exists()) {
                System.out.println("Input dir does not exist");
                System.exit(1);
            }
            dir = dir.getAbsoluteFile();

            outdir = new File(args[1]);
            if (!outdir.exists()) {
                System.out.println("Output dir does not exist");
                System.exit(1);
            }
            outdir = outdir.getAbsoluteFile();
        } else {
            outdir = null;
            dir = null;
            System.out.println("Wrong arguments");
            System.exit(1);
        }

        TexturePacker.Settings settings;
        try {
            settings = new Json().fromJson(TexturePacker.Settings.class, new FileReader(new File(dir,  "pack.json")));
        }
        catch (FileNotFoundException e)
        {
            settings = new TexturePacker.Settings();
        }

        for (File file : dir.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.exists() && pathname.isDirectory();
            }
        })) {
            TexturePacker.process(settings, file.getAbsolutePath(), outdir.getAbsolutePath(), file.getName() + ".atlas");
        }
    }
}
