import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class tar {
    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: tar <output archive> [input directory]");
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
        TarArchiveOutputStream tarOutput = createArchive(archive);
        addEntryToArchive(archive.getName(), tarOutput, inputDirectory, null);
        tarOutput.close();
    }

    private static TarArchiveOutputStream createArchive(File archive) throws IOException {
        OutputStream output = new BufferedOutputStream(new FileOutputStream(archive));
        if (archive.getName().endsWith(".gz")) {
            output = new GzipCompressorOutputStream(output);
        }
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(output);
        tarOutput.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR);
        tarOutput.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutput.setAddPaxHeadersForNonAsciiNames(true);
        return tarOutput;
    }

    private static void addEntryToArchive(String archiveName, TarArchiveOutputStream tarOutput, File file, String inputDirectory) throws IOException {
        String filePath = file.getName();
        if (inputDirectory != null) {
            filePath = inputDirectory + File.separator + filePath;
        }
        if (filePath.equals(archiveName) || filePath.equals("." + File.separator + archiveName)) {
            System.out.println("Skipping the archive \"" + filePath + "\" itself...");
        } else if (Files.isSymbolicLink(file.toPath())) {
            throw new IOException("Symbolic link \"" + file.getName() + "\" is not supported.");
        } else if (file.isDirectory()) {
            addDirectoryToArchive(archiveName, tarOutput, file, filePath);
        } else if (file.isFile()){
            addFileToArchive(tarOutput, file, filePath);
        } else {
            throw new IOException("Unrecognized item \"" + file.getName() + "\" is not supported.");
        }
    }

    private static void addDirectoryToArchive(String archiveName, TarArchiveOutputStream tarOutput, File file, String filePath) throws IOException {
        System.out.println("Entering \"" + filePath + "\"...");
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                addEntryToArchive(archiveName, tarOutput, child, filePath);
            }
        }
    }

    private static void addFileToArchive(TarArchiveOutputStream tarOutput, File file, String filePath) throws IOException {
        System.out.println("Packing \"" + filePath + "\"...");
        tarOutput.putArchiveEntry(new TarArchiveEntry(file, filePath));
        IOUtils.copy(new FileInputStream(file), tarOutput);
        tarOutput.closeArchiveEntry();
    }
}