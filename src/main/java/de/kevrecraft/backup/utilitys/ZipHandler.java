package de.kevrecraft.backup.utilitys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipHandler {

    private File file;

    public ZipHandler(File file) {
        this.file = file;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public void zip(File source, boolean override) throws Exception {
        if(override) {
            if(this.exists()) {
                this.file.delete();
            }
        } else {
            if(this.exists()) {
                throw new Exception("Kann die Datei nicht Zipen! Es exestiert bereits eine!");
            }
        }

        FileOutputStream fos = new FileOutputStream(this.file);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        zipFile(source, source.getName(), zipOut);

        zipOut.close();
        fos.close();
    }

    private void zipFile(File source, String name, ZipOutputStream zipOut) throws IOException {
        if (source.isHidden()) {
            return;
        }
        if (source.isDirectory()) {
            if(name.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(name));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(name + "/"));
                zipOut.closeEntry();
            }
            File[] children = source.listFiles();
            for (File child : children) {
                zipFile(child, name + "/" + child.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(source);
        ZipEntry zipEntry = new ZipEntry(name);
        zipOut.putNextEntry(zipEntry);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) >= 0) {
            zipOut.write(buffer, 0, length);
        }
        fis.close();
    }

    public void unzip(File destination, boolean override) throws Exception{
        if(override) {
            if(destination.exists()) {
                destination.delete();
            }
        } else {
            if (destination.exists()) {
                throw new Exception("Die Zieldatei exestiert bereits!");
            }
        }

        if(!this.exists()) {
            throw new Exception("Zip file exestiert nicht!");
        }

        byte[] buffer = new byte[1024];
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(this.file));

        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            File entryFile = new File(destination, entry.getName());
            if (entry.isDirectory()) {
                entryFile.mkdirs();
            } else {
                entryFile.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(entryFile);
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipInputStream.closeEntry();
        }
        zipInputStream.close();
    }

}
