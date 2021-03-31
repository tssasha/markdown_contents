import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) throws IOException {
        try {
            String text = '\n' + Files.readString(Path.of(args[0])) + '\n';
            String result = "";
            int[] rangs = new int[6];
            Set<String> headers = new HashSet<>();

            Pattern pattern = Pattern.compile("\n\s{0,3}(\\*\s{1,4})*(#{1,6})\s([^\n]+)");
            Pattern name_pattern = Pattern.compile("\s*(\\**)(([^\s]+\s+)*[^\s]+)(\\1)\s*");
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                Matcher name_matcher = name_pattern.matcher(matcher.group(3));

                if (name_matcher.find()) {
                    String link = name_matcher.group(2);
                    link = link.replaceAll("[!\"#$%&'()*+,./\\\\:;<=>?@\\[\\]^`{|}~]", "").toLowerCase();
                    link = link.replaceAll(" ", "-");

                    if (link.length() > 0) {
                        if (!headers.contains(link)) {
                            headers.add(link);
                        }
                        else {
                            Integer i = 1;
                            while (headers.contains(link + '-' + i.toString())) {
                                ++i;
                            }
                            link += '-' + i.toString();
                            headers.add(link);
                        }
                        Integer rang = matcher.group(2).length() - 1;
                        for (int i = rang + 1; i < 6; ++i) {
                            rangs[i] = 0;
                        }
                        ++rangs[rang];
                        for (int i = 0; i < rang; ++i) {
                            result += "    ";
                        }
                        Integer print_rang = rangs[rang];
                        result += print_rang.toString() + ". " + "[" + name_matcher.group(2).replaceAll(" +", " ") + "]" + "(#" + link + ")\n";
                    }
                }
            }
            result += text;
            System.out.print(result);

            FileWriter fstream1 = new FileWriter(args[0]);
            BufferedWriter out1 = new BufferedWriter(fstream1);
            out1.write(result);
            out1.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
