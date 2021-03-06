import java.util.*;

/**
 * 可以剪枝：后边的应该大于前边的最小值；前边的应该小于后边的最大值
 * Created by weishubin on 2018/11/9.
 */
public class WildcardMatchingV2 {
    public boolean isMatch(String s, String p) {
        p = mergeStar(p);
//        System.out.println(p);
        Map<Integer, Pos>  groupMap = group(p);
//        System.out.println(groupMap);
        if (!createGroupIndex(s, p, groupMap)) {
            return false;
        }
//        System.out.println(groupMap);
        mergeQuesionMark(s, p, groupMap);
//        System.out.println(groupMap);

        if(!cut(groupMap)) {
            return false;
        }
        return isMatch(s, 0, s.length(), p, 0, p.length(), groupMap);
    }


    private boolean isMatch(String s, int sStart, int sEnd, String p, int pStart, int pEnd, Map<Integer, Pos>  groupMap) {
        //System.out.println(s.substring(sStart, sEnd) + "\n" + p.substring(pStart, pEnd) + "\n");
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
        Pos pos = groupMap.get(pStart);
        if (pos == null) {
            throw new RuntimeException("pos error");
        }

        if (pos.type == CharType.Wild) {
            if (pos.start + pos.len == p.length()) { //*是最后一个字符
                return true;
            } else {
                Pos nextPos = groupMap.get(pos.start + pos.len);
                if (nextPos.type == CharType.Normal) { //字符
                    for (int i : nextPos.matchIndex) {
                        if (i >= sStart) {
                            boolean b = isMatch(s, i + nextPos.len, sEnd, p, nextPos.start + nextPos.len, pEnd, groupMap);
                            if (b) {
                                return true;
                            }
                        }
                    }
                    return false;
                } else if (nextPos.type == CharType.Single) { //?
                    for (int i = sStart; i < sEnd; i++) {
                        if (isMatch(s, i, sEnd, p, pos.start + pos.len, pEnd, groupMap)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    System.err.println("error1");
                    return false;
                }
            }
        } else if(pos.type == CharType.Single) {
            if (pos.matchIndex.size() > 0) { //?号右边是字母
                for (int m : pos.matchIndex) {
                    if (m == sStart) {
                        return isMatch(s, sStart + pos.len, sEnd, p, pStart + pos.len, pEnd, groupMap);
                    }
                }
                return false;
            } else {
                return isMatch(s, sStart + pos.len, sEnd, p, pStart + pos.len, pEnd, groupMap);
            }
        } else {
            for (int index : pos.matchIndex) {
                if (index == sStart) {
                    return isMatch(s, sStart + pos.len, sEnd, p, pStart + pos.len, pEnd, groupMap);
                }
            }
            return false;
        }
    }

    private String mergeStar(String p) {
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
        return sb.toString();
    }

    /**
     * 分组
     */
    private Map<Integer, Pos>  group(String p) {
        Map<Integer, Pos> parseResult = new HashMap<Integer, Pos>();
        if (p.length() == 0) {
            return parseResult;
        }
        StringBuilder sb = new StringBuilder();
        CharType preCharType = null; //init
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            CharType t = CharType.convert(c);
            if (t == preCharType) {
                sb.append(c);
            } else {
                if (preCharType != null && sb.length() > 0) {
                    String s = sb.toString();
                    int start = i - s.length();
                    Pos pos = new Pos(preCharType, start, s.length());
                    parseResult.put(start, pos);
                }


                sb.setLength(0);
                sb.append(c);
                preCharType = t;
            }
        }
        if (sb.length() > 0) {
            String s = sb.toString();
            int start = p.length() - s.length();
            Pos pos = new Pos(preCharType, start, s.length());
            parseResult.put(start, pos);
        }
        return parseResult;
    }

    private boolean createGroupIndex(String s, String p, Map<Integer, Pos>  groupMap ) {
        for (Pos pos : groupMap.values()) {
            if (pos.type == CharType.Normal) {
                String group = p.substring(pos.start, pos.start + pos.len);
                int beginIndx = 0;
                int index = s.indexOf(group, beginIndx);
                while (index >= 0) {
                    pos.matchIndex.add(index);
                    if (index + 1 >= s.length()) {
                        break;
                    }
                    index = s.indexOf(group, index + 1);
                }
                if (pos.matchIndex.size() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 分析"?"
     * @param s
     * @param p
     * @param groupMap
     */
    private boolean mergeQuesionMark(String s, String p, Map<Integer, Pos>  groupMap) {
        Set<Integer> startSet = groupMap.keySet();
        List<Integer> sortedList = new ArrayList<Integer>(startSet);
        Collections.sort(sortedList);
        for (int i = 0; i < sortedList.size(); i++) {
            Pos pos = groupMap.get(sortedList.get(i));
            CharType rightType = CharType.LineEnd;//$
            Pos rightPos = null;
            if (pos.type != CharType.Single) {
                continue;
            }
            if (i < sortedList.size() - 1) {
                int rightIndex = sortedList.get(i + 1);
                rightPos = groupMap.get(rightIndex);
                rightType = rightPos.type;
            }

            if (rightType == CharType.Single) {
                System.err.println("merge error!");
                return false;
            }

            if (rightType == CharType.Normal) {
                boolean find = false;
                for (int rightM : rightPos.matchIndex) {
                    if (rightM - pos.len >= 0) {
                        pos.matchIndex.add(rightM - pos.len);
                    }
                }
                if (!find) {
                    return false;
                }
            }
            if (rightType == CharType.LineEnd) {
                if (s.length() - pos.len >= 0) {
                    pos.matchIndex.add(s.length() - pos.len);
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private int typeConvert(char c) {
        if (c == '*') {
            return 0;
        }
        if (c == '?') {
            return 1;
        }
        return 2;
    }

    private static char typeConvert(int t) {
        if (t == 0) {
            return '*';
        }
        if (t == 1) {
            return '?';
        }
        return 'c';
    }
    static class Pos {
        Pos(CharType t, int s, int len) {
            this.type = t;
            this.start = s;
            this.len = len;
        }
        CharType type; //0-*,1-?,2-other
        int start;//在p字符串中的开始位置
        int len;//长度
        List<Integer> matchIndex = new ArrayList(); //匹配位置

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(type);
            sb.append('-');
            sb.append(start);
            sb.append('-');
            sb.append(len);
            sb.append('-');
            sb.append(matchIndex);
            return sb.toString();
        }
    }

    enum CharType {
        LineStart(-1),
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
    private boolean cut(Map<Integer, Pos>  groupMap) {
        List<Integer> groupStartList = new ArrayList<Integer>(groupMap.keySet());
        Collections.sort(groupStartList);

        //后边的应该大于前边的最小值；前边的应该小于后边的最大值
        int min = 0;
        for (int  groupStart : groupStartList) {
            List<Integer> matchIndex = groupMap.get(groupStart).matchIndex;
            if (matchIndex.size() > 0 &&
                    (groupMap.get(groupStart).type == CharType.Normal || groupMap.get(groupStart).type == CharType.Single)) {
                Collections.sort(matchIndex);
                Iterator<Integer> iter = matchIndex.iterator();
                 while (iter.hasNext()) {
                    if (iter.next() < min) {
                        iter.remove();
                    }
                }

                if (matchIndex.size() == 0) {
                    return false;
                }
                min = matchIndex.get(0);
                System.out.println(matchIndex.size());
            }
        }

        System.out.println("------");

        int preMax = Integer.MAX_VALUE;
        for (int i = groupStartList.size() - 1; i >= 0; i--) {
            int groupStart = groupStartList.get(i);
            List<Integer> matchIndex = groupMap.get(groupStart).matchIndex;
            if (matchIndex.size() > 0 &&
                    (groupMap.get(groupStart).type == CharType.Normal || groupMap.get(groupStart).type == CharType.Single)) {
                Iterator<Integer> iter = matchIndex.iterator();
                while (iter.hasNext()) {
                    if (iter.next() > preMax) {
                        iter.remove();
                    }
                }

                if (matchIndex.size() == 0) {
                    return false;
                }
                preMax = matchIndex.get(matchIndex.size() - 1);
                System.out.println(matchIndex.size());
            }
        }

        return true;
    }



    public static void main(String[] args) {
        WildcardMatchingV2 s = new WildcardMatchingV2();
//        assert s.isMatch("adceb", "*a*b") == true;
        long t = System.currentTimeMillis();
//        s.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
//                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb");
//        assert s.isMatch("acdcb", "a*c?b") == false;
//        assert s.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
//                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb") == false;
//                assert s.isMatch("", "a") == false;
//        assert s.isMatch("cb", "*a") == false;
//        assert s.isMatch("aa", "*") == true;
//        assert s.isMatch("b", "?b") == false;
//        assert s.isMatch("aa", "a") == false;
//        assert s.isMatch("adceb", "*a*b") == true;
//        s.isMatch("", "???");


//        s.isMatch("aaaaaabbaabaaaaabababbabbaababbaabaababaaaaabaaaabaaaabababbbabbbbaabbababbbbababbaaababbbabbbaaaaaaabbaabbbbababbabbaaabababaaaabaaabaaabbbbbabaaabbbaabbbbbbbaabaaababaaaababbbbbaabaaabbabaabbaabbaaaaba",
//                "*bbb**a*******abb*b**a**bbbbaab*b*aaba*a*b**a*abb*aa****b*bb**abbbb*b**bbbabaa*b**ba**a**ba**b*a*a**aaa");
        System.out.println(System.currentTimeMillis() - t);


        //"aaaaaabbaabaaaaabababbabbaababbaabaababaaaaabaaaabaaaabababbbabbbbaabbababbbbababbaaababbbabbbaaaaaaabbaabbbbababbabbaaabababaaaabaaabaaabbbbbabaaabbbaabbbbbbbaabaaababaaaababbbbbaabaaabbabaabbaabbaaaaba"
//        "*bbb**a*******abb*b**a**bbbbaab*b*aaba*a*b**a*abb*aa****b*bb**abbbb*b**bbbabaa*b**ba**a**ba**b*a*a**aaa"


//        "baaabbabbbaabbbbbbabbbaaabbaabbbbbaaaabbbbbabaaaaabbabbaabaaababaabaaabaaaabbabbbaabbbbbaababbbabaaabaabaaabbbaababaaabaaabaaaabbabaabbbabababbbbabbaaababbabbaabbaabbbbabaaabbababbabababbaabaabbaaabbba"
//        "*b*ab*bb***abba*a**ab***b*aaa*a*b****a*b*bb**b**ab*ba**bb*bb*baab****bab*bbb**a*a*aab*b****b**ba**abba"
        s.isMatch("baaabbabbbaabbbbbbabbbaaabbaabbbbbaaaabbbbbabaaaaabbabbaabaaababaabaaabaaaabbabbbaabbbbbaababbbabaaabaabaaabbbaababaaabaaabaaaabbabaabbbabababbbbabbaaababbabbaabbaabbbbabaaabbababbabababbaabaabbaaabbba",
                "*b*ab*bb***abba*a**ab***b*aaa*a*b****a*b*bb**b**ab*ba**bb*bb*baab****bab*bbb**a*a*aab*b****b**ba**abba");
    }

}
