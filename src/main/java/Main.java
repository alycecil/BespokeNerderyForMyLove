import java.io.File;

public class Main {

    public static final String FAILED_DIRECTORY = "misc";

    public static void main(String... args) {
        File pwd;
        if (args.length == 1) {
            pwd = new File(args[0]);
        } else {
            pwd = new File(".");
        }

        sort(pwd);
    }

    private static void sort(File pwd) {
        for (File file : pwd.listFiles()) {
            if (file.isDirectory()) {
//noop
            } else {
                sortFile(pwd, file);
            }
        }
    }

    private static void sortFile(File pwd, File file) {
        String folder = chooseFolderName(file);
        File folderFile = asFolder(pwd, folder);
        File newFile = new File(folderFile, file.getName());

        boolean renameTo = file.renameTo(newFile);
        if (renameTo) {
            System.out.println(".");
        } else {
            System.err.println("Unable to move [file=" + file.getAbsolutePath() + "] to [destination=" + newFile.getAbsolutePath() + "]");
        }

    }

    private static File asFolder(File pwd, String folder) {
        File folderFile = new File(pwd, folder);
        if (folderFile.exists()) {
            if (folderFile.isDirectory()) {
                //done
            } else {
                folderFile = asFolder(pwd, folder + "x");
            }
        } else {
            folderFile.mkdir();
        }
        return folderFile;
    }

    private static String chooseFolderName(File file) {
        String name = file.getName();

        String folderTo = FAILED_DIRECTORY;

        for (SearchTerms value : SearchTerms.values()) {
            if(value.belongs(name)){
                folderTo = value.name();
                break;
            }
        }

        return folderTo;
    }

    enum SearchTerms {
//Order Matters, first match wins
        Bass("bas"),
        Bells("bell", "cow", "chime", "ring"),
        Claps("clap"),
        Kicks("kick", "kck"),
        Shakers("shake"),
        Stabs("stb", "stabs"),
        Vocals("vocal"),
        Noise("noise"),
        Percussion("perc"),
        Snares("snare"),
        Toms("Tom"),
        Rims("rim"),
        Cymbals("Cymbal", "Crash", "Open", "Closed", "Ride", "Hat");

        String[] terms;

        SearchTerms(String... terms) {
            this.terms = terms;
            for (int i = 0; i < terms.length; i++) {
                terms[i] = terms[i].toLowerCase();
            }
        }

        boolean belongs(String name) {
            name = name.toLowerCase();
            for (String term : this.terms) {
                if (name.contains(term)) {
                    return true;
                }
            }
            return false;
        }
    }
}
