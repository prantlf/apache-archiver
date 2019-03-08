import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.nio.file.Files;

public class untar {
    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: untar [l|x] <input archive> <output directory>");
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
                        if (args.length < 3) {
                            System.out.println("Missing output directory.");
                        } else {
                            File outputDirectory = new File(args[2]);
                            uncompress(archive, outputDirectory);
                        }
                        break;
                    default:
                        System.out.println("Unknown command.");
                }
            }
        }
    }

    private static void list(File archive) throws IOException {
        TarArchiveInputStream tarInput = openArchive(archive);

        while (true) {
            TarArchiveEntry tarEntry = tarInput.getNextTarEntry();
            if (tarEntry == null) {
                break;
            }
            String entryName = tarEntry.getName();
            if (tarEntry.isSymbolicLink()) {
                System.out.println("Symbolic link \"" + entryName + "\"");
            } else if (tarEntry.isDirectory()) {
                System.out.println("Directory \"" + entryName + "\"");
            } else if (tarEntry.isFile()) {
                System.out.println("File \"" + entryName + "\"");
            } else {
                throw new IOException("Unknown archive entry type.");
            }
        }
        tarInput.close();
    }

    private static void uncompress(File archive, File outputDirectory) throws IOException {
        TarArchiveInputStream tarInput = openArchive(archive);

        outputDirectory.mkdirs();

        while (true) {
            TarArchiveEntry tarEntry = tarInput.getNextTarEntry();
            if (tarEntry == null) {
                break;
            }
            String entryName = tarEntry.getName();
            if (tarEntry.isSymbolicLink()) {
                String linkName = tarEntry.getLinkName();
                System.out.println("Linking \"" + linkName + "\" to \"" + entryName + "\"...");
                File outputFile = new File(outputDirectory, entryName);
                File parentDirectory = outputFile.getParentFile();
                File linkFile = new File(parentDirectory, linkName);
                Files.createSymbolicLink(outputFile.toPath(), linkFile.toPath().toAbsolutePath());
            } else if (tarEntry.isDirectory()) {
                System.out.println("Creating \"" + entryName + "\"...");
                File outputFile = new File(outputDirectory, entryName);
                outputFile.mkdirs();
            } else if (tarEntry.isFile()) {
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
                while((readSize = tarInput.read(readBuffer)) != -1) {
                    outputStream.write(readBuffer, 0, readSize);
                }
                outputStream.close();
            } else {
                throw new IOException("Unknown archive entry type.");
            }
        }
        tarInput.close();
    }

    private static TarArchiveInputStream openArchive(File archive) throws IOException {
        InputStream input = new BufferedInputStream(new FileInputStream(archive));
        if (archive.getName().endsWith(".gz")) {
            input = new GzipCompressorInputStream(input);
        }
        return new TarArchiveInputStream(input);
    }
}