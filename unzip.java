import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class unzip {
    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: unzip l|x <input archive> [output directory]");
        } else {
            String command = args[0];
            if (args.length < 2) {
                System.out.println("Missing input archive.");
            } else {
                File archive = new File(args[1]);
                switch (command) {
                    case "l":
                        list(archive);
                        break;
                    case "x":
                        String outputDirectoryArg = ".";
                        if (args.length > 2) {
                            outputDirectoryArg = args[2];
                        }
                        File outputDirectory = new File(outputDirectoryArg);
                        uncompress(archive, outputDirectory);
                        break;
                    default:
                        System.out.println("Unknown command.");
                }
            }
        }
    }

    private static void list(File archive) throws IOException {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(
            new BufferedInputStream(new FileInputStream(archive))
        );

        while (true) {
            ZipArchiveEntry zipEntry = zipInput.getNextZipEntry();
            if (zipEntry == null) {
                break;
            }
            String entryName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                System.out.println("Directory \"" + entryName + "\"");
            } else {
                System.out.println("File \"" + entryName + "\"");
            }
        }
        zipInput.close();
    }

    private static void uncompress(File archive, File outputDirectory) throws IOException {
        ZipArchiveInputStream zipInput = new ZipArchiveInputStream(
            new BufferedInputStream(new FileInputStream(archive))
        );

        outputDirectory.mkdirs();

        while (true) {
            ZipArchiveEntry zipEntry = zipInput.getNextZipEntry();
            if (zipEntry == null) {
                break;
            }
            String entryName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                System.out.println("Creating \"" + entryName + "\"...");
                File outputFile = new File(outputDirectory, entryName);
                outputFile.mkdirs();
            } else {
                System.out.println("Unpacking \"" + entryName + "\"...");
                File outputFile = new File(outputDirectory, entryName);
                File parentDirectory = outputFile.getParentFile();
                parentDirectory.mkdirs();
                outputFile.createNewFile();
                byte[] readBuffer = new byte[32768];
                BufferedOutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(outputFile)
                );
                int readSize = 0;
                while((readSize = zipInput.read(readBuffer)) != -1) {
                    outputStream.write(readBuffer, 0, readSize);
                }
                outputStream.close();
            }
        }
        zipInput.close();
    }
}