/**
 * Created by weishubin on 2018/11/8.
 */
public class WildcardMatching {
    public boolean isMatch(String s, String p) {
        //优化p，多个连续的*合并成一个
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c != '*') {
                sb.append(c);
            } else {
                if (i == 0) {
                    sb.append(c);
                } else if (p.charAt(i - 1) != '*') {
                    sb.append(c);
                }
            }
        }
        p = sb.toString();
//        System.out.println(p);
        return isMatch(s, 0, s.length(), p, 0, p.length());
    }

    private boolean isMatch(String s, int sStart, int sEnd, String p, int pStart, int pEnd) {
//        System.out.println(s.substring(sStart, sEnd) + ",,," + p.substring(pStart, pEnd));
        if (sStart == sEnd && pStart == pEnd) {
            return true;
        } else if (sStart == sEnd) {
            for (int i = pStart; i < pEnd; i++) {
                if (p.charAt(i) != '*') {
                    return false;
                }
            }
            return true;
        } else if (pStart == pEnd) {
            return false;
        }
        char sFirst = s.charAt(sStart);
        char pFirst = p.charAt(pStart);

        if (pFirst == '*') {
            for (int i = sStart; i <= sEnd; i++) {
                boolean b = isMatch(s, i, sEnd, p, pStart + 1, pEnd);
                if (b) {
                    return true;
                }
            }
            return false;
        } else if(pFirst == '?') {
            return isMatch(s, sStart + 1, sEnd, p, pStart + 1, pEnd);
        } else {
            if (sFirst == pFirst) {
                return isMatch(s, sStart + 1, sEnd, p, pStart + 1, pEnd);
            } else {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        WildcardMatching s = new WildcardMatching();
//        "abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb"
//        "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb"
        assert s.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb") == false;
//        assert s.isMatch("", "a") == false;
//        assert s.isMatch("cb", "*a") == false;
//        assert s.isMatch("aa", "*") == true;
//        assert s.isMatch("b", "?b") == false;
//        assert s.isMatch("aa", "a") == false;
//        assert s.isMatch("adceb", "*a*b") == true;
//
//        assert s.isMatch("acdcb", "a*c?b") == false;
    }
}
