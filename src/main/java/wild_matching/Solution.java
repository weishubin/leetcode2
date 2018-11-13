package wild_matching;

import java.util.Map;

/**
 * leetcode测试题目里没有测试这种场景："zaacbz", "*a?b*
 * 这个代码里也暂没实现
 * Created by weishubin on 2018/11/13.
 */
public class Solution {

    public boolean isMatch(String s, String p) {
        return this.isMatch(false, s, 0, s.length(), p, 0, p.length());
    }

    private boolean isMatch(boolean preIsWild, String s, int sStart, int sEnd, String p, int pStart, int pEnd) {
        if (sStart < 0 || pStart < 0) {
            return false;
        }

        if (sStart == sEnd && pStart == pEnd) {
            return true;
        } else if (sStart > sEnd || pStart > pEnd) {
            return false;
        }

        if (pStart == pEnd) { //aa,a
            if (sStart < sEnd) {
                return false;
            }
        }

        if (sStart == sEnd && pStart < pEnd) {
            for (int i = pStart; i < pEnd; i++) {
                if (p.charAt(i) != '*') {
                    return false;
                }
            }
            return true;
        }

//        System.out.println(s.substring(sStart, sEnd) + "\n" + p.substring(pStart, pEnd) + "\n");

        char pStartChar = p.charAt(pStart);
        CharType charType = CharType.convert(pStartChar);

        int pGroupEnd = pStart + 1;
        while (pGroupEnd < pEnd) {
            char c = p.charAt(pGroupEnd);
            CharType cT = CharType.convert(c);
            if (cT == charType) {
                pGroupEnd++;
            } else {
                break;
            }
        }

        String sub = p.substring(pStart, pGroupEnd);

        if (charType == CharType.Wild) {
            int lastIndex = p.lastIndexOf('*');
            if (lastIndex < pGroupEnd) { //最后一个*，从后边匹配
                int ss = s.length() - p.length() + lastIndex + 1;
                if (ss < sStart) {
                    return false;
                }
                return isMatch(false, s,  ss, sEnd, p, lastIndex + 1, pEnd);
            } else {
                return isMatch(true, s, sStart, sEnd, p, pGroupEnd, pEnd);
            }
        } else if (charType == CharType.Normal) {
            int index = s.indexOf(sub, sStart);
            if (index < 0) {
                return false;
            } else {
                if (!preIsWild && index != sStart) {
                    return false;
                }
                return isMatch(false, s, index + sub.length(), sEnd, p, pStart + sub.length(), pEnd);

            }
        } else {
            return isMatch(preIsWild, s, sStart + sub.length(), sEnd, p, pGroupEnd, pEnd);
        }

    }


    enum CharType {
        Wild(0),
        Single(1),
        Normal(2),
        LineEnd(-2);
        private CharType(int t) {
            this.type = t;
        }

        int type;

        public static CharType convert(char c) {
            if (c == '*') {
                return Wild;
            }
            if (c == '?') {
                return Single;
            }
            return Normal;
        }

        public String toString() {
            if (type == 0) {
                return "*";
            }
            if (type == 1) {
                return "?";
            }
            return "c";
        }
    }



    public static void main(String[] args) {
        Solution s = new Solution();
        long t = System.currentTimeMillis();


//        assert s.isMatch("zaacbz", "*a?b*") == true;
        assert s.isMatch("zacabz", "*a?b*") == false;
        assert s.isMatch("ippi", "*?i*pi") == false;
        assert s.isMatch("mississippi", "m??*ss*?i*pi") == false;
        assert s.isMatch("bbabab", "**?a*") == true;
        assert s.isMatch("aa", "a") == false;
        assert s.isMatch("", "*") == true;
        assert s.isMatch("babaaababaabababbbbbbaabaabbabababbaababbaaabbbaaab", "***bba**a*bbba**aab**") == true;
        assert s.isMatch("b", "??") == false;
        assert s.isMatch("abce", "abc*??") == false;
        assert s.isMatch("aaaa", "***a") == true;
        assert s.isMatch("abaab", "*?a?") == true;
        assert s.isMatch("aaaba", "*?*?a") == true;
        assert s.isMatch("babaaababaabababbbbbbaabaabbabababbaababbaaabbbaaab", "***bba**a*bbba**aab**b") == false;
        assert s.isMatch("abbbba", "a**a*?") == false;
        assert s.isMatch("abc", "abc?*") == false;
        assert s.isMatch("abcd", "ab*cd") == true;
        assert s.isMatch("ab", "*ab") == true;
        assert s.isMatch("adceb", "*a*b") == true;
        assert s.isMatch("bbbbb", "*b") == true;
        assert s.isMatch("a", "a*") == true;
        assert s.isMatch("a", "a") == true;
        assert s.isMatch("a", "a") == true;
        assert s.isMatch("", "*") == true;
        s.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb");
        assert s.isMatch("acdcb", "a*c?b") == false;
        assert s.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb") == false;
        assert s.isMatch("", "a") == false;
        assert s.isMatch("cb", "*a") == false;
        assert s.isMatch("aa", "*") == true;
        assert s.isMatch("b", "?b") == false;
        assert s.isMatch("adceb", "*a*b") == true;
        s.isMatch("", "???");


        boolean b = s.isMatch("aaaaaabbaabaaaaabababbabbaababbaabaababaaaaabaaaabaaaabababbbabbbbaabbababbbbababbaaababbbabbbaaaaaaabbaabbbbababbabbaaabababaaaabaaabaaabbbbbabaaabbbaabbbbbbbaabaaababaaaababbbbbaabaaabbabaabbaabbaaaaba",
                "*bbb**a*******abb*b**a**bbbbaab*b*aaba*a*b**a*abb*aa****b*bb**abbbb*b**bbbabaa*b**ba**a**ba**b*a*a**aaa");
        System.out.println(b);


        //"aaaaaabbaabaaaaabababbabbaababbaabaababaaaaabaaaabaaaabababbbabbbbaabbababbbbababbaaababbbabbbaaaaaaabbaabbbbababbabbaaabababaaaabaaabaaabbbbbabaaabbbaabbbbbbbaabaaababaaaababbbbbaabaaabbabaabbaabbaaaaba"
//        "*bbb**a*******abb*b**a**bbbbaab*b*aaba*a*b**a*abb*aa****b*bb**abbbb*b**bbbabaa*b**ba**a**ba**b*a*a**aaa"


//        "baaabbabbbaabbbbbbabbbaaabbaabbbbbaaaabbbbbabaaaaabbabbaabaaababaabaaabaaaabbabbbaabbbbbaababbbabaaabaabaaabbbaababaaabaaabaaaabbabaabbbabababbbbabbaaababbabbaabbaabbbbabaaabbababbabababbaabaabbaaabbba"
//        "*b*ab*bb***abba*a**ab***b*aaa*a*b****a*b*bb**b**ab*ba**bb*bb*baab****bab*bbb**a*a*aab*b****b**ba**abba"
        b = s.isMatch("baaabbabbbaabbbbbbabbbaaabbaabbbbbaaaabbbbbabaaaaabbabbaabaaababaabaaabaaaabbabbbaabbbbbaababbbabaaabaabaaabbbaababaaabaaabaaaabbabaabbbabababbbbabbaaababbabbaabbaabbbbabaaabbababbabababbaabaabbaaabbba",
                "*b*ab*bb***abba*a**ab***b*aaa*a*b****a*b*bb**b**ab*ba**bb*bb*baab****bab*bbb**a*a*aab*b****b**ba**abba");
        System.out.println(b);

        assert s.isMatch("abcdd", "abc*d") == true;
        assert s.isMatch("aa", "aa") == true;

        System.out.println(System.currentTimeMillis() - t);
    }

}
