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
            System.exit(1);
        }
        File archive = new File(args[0]);
        String inputDirectory = ".";
        if (args.length > 1) {
            inputDirectory = args[1];
        }
        compress(archive, new File(inputDirectory));
    }

    private static void compress(File archive, File inputDirectory) throws IOException {
        ZipArchiveOutputStream zipOutput = createArchive(archive);
        addEntryToArchive(archive.getName(), zipOutput, inputDirectory, null);
        zipOutput.close();
    }

    private static ZipArchiveOutputStream createArchive(File archive) throws IOException {
        ZipArchiveOutputStream zipOutput = new ZipArchiveOutputStream(
            new BufferedOutputStream(new FileOutputStream(archive))
        );
        zipOutput.setLevel(9);
        zipOutput.setUseZip64(Zip64Mode.AsNeeded);
        zipOutput.setEncoding("UTF-8");
        zipOutput.setFallbackToUTF8(true);
        zipOutput.setUseLanguageEncodingFlag(true);
        zipOutput.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.NOT_ENCODEABLE);
        return zipOutput;
    }

    private static void addEntryToArchive(String archiveName, ZipArchiveOutputStream zipOutput, File file, String inputDirectory) throws IOException {
        String filePath = file.getName();
        if (inputDirectory != null) {
            filePath = inputDirectory + File.separator + filePath;
        }
        if (filePath.equals(archiveName) || filePath.equals("." + File.separator + archiveName)) {
            System.out.println("Skipping the archive \"" + filePath + "\" itself...");
        } else if (Files.isSymbolicLink(file.toPath())) {
            throw new IOException("Symbolic link \"" + file.getName() + "\" is not supported.");
        } else if (file.isDirectory()) {
            addDirectoryToArchive(archiveName, zipOutput, file, filePath);
        } else if (file.isFile()){
            addFileToArchive(zipOutput, file, filePath);
        } else {
            throw new IOException("Unrecognized item \"" + file.getName() + "\" is not supported.");
        }
    }

    private static void addDirectoryToArchive(String archiveName, ZipArchiveOutputStream zipOutput, File file, String filePath) throws IOException {
        System.out.println("Entering \"" + filePath + "\"...");
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                addEntryToArchive(archiveName, zipOutput, child, filePath);
            }
        }
    }

    private static void addFileToArchive(ZipArchiveOutputStream zipOutput, File file, String filePath) throws IOException {
        System.out.println("Packing \"" + filePath + "\"...");
        zipOutput.putArchiveEntry(new ZipArchiveEntry(filePath));
        IOUtils.copy(new FileInputStream(file), zipOutput);
        zipOutput.closeArchiveEntry();
    }
}