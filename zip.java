import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class zip {
    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: zip <output archive> [input directory]");
        } else {
            File archive = new File(args[0]);
            String inputDirectory = ".";
            if (args.length > 1) {
                inputDirectory = args[1];
            }
            compress(archive, new File(inputDirectory));
        }
    }

    private static void compress(File archive, File inputDirectory) throws IOException {
        ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(
            new BufferedOutputStream(new FileOutputStream(archive))
        );
        zipOutput.setLevel(9);
        zipOutput.setUseZip64(Zip64Mode.AsNeeded);
        zipOutput.setEncoding("UTF-8");
        zipOutput.setFallbackToUTF8(true);
        zipOutput.setUseLanguageEncodingFlag(true);
        zipOutput.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.NOT_ENCODEABLE);

        addFileToArchive(archive.getName(), zipOutput, inputDirectory, null);
        zipOutput.close();
    }

    private static void addFileToArchive(String archiveName, ZipArchiveOutputStream zipOutput, File file, String inputDirectory) throws IOException {
        String filePath = file.getName();
        if (inputDirectory != null) {
            filePath = inputDirectory + File.separator + filePath;
        }
        if (filePath.equals(archiveName) || filePath.equals("." + File.separator + archiveName)) {
            System.out.println("Skipping the archive \"" + filePath + "\" itself...");
        } else if (Files.isSymbolicLink(file.toPath())) {
            throw new IOException("Symbolic link \"" + file.getName() + "\" is not supported.");
        } else if (file.isDirectory()) {
            System.out.println("Entering \"" + filePath + "\"...");
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileToArchive(archiveName, zipOutput, child, filePath);
                }
            }
        } else if (file.isFile()){
            System.out.println("Packing \"" + filePath + "\"...");
            zipOutput.putArchiveEntry(new ZipArchiveEntry(filePath));
            IOUtils.copy(new FileInputStream(file), zipOutput);
            zipOutput.closeArchiveEntry();
        } else {
            throw new IOException("Unrecognized item \"" + file.getName() + "\" is not supported.");
        }
    }
}