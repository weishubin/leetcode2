import java.util.*;

/**
 * Created by weishubin on 2018/11/9.
 */
public class WildcardMatchingV2 {
    public boolean isMatch(String s, String p) {
        p = mergeStar(p);
        System.out.println(p);
        Map<Integer, Pos>  groupMap = group(p);
        System.out.println(groupMap);
        if (!createGroupIndex(s, p, groupMap)) {
            return false;
        }
        System.out.println(groupMap);
        return isMatch(s, 0, s.length(), p, 0, p.length(), groupMap);
    }

    private boolean isMatch(String s, int sStart, int sEnd, String p, int pStart, int pEnd, Map<Integer, Pos>  groupMap) {
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
            if (pStart + 1 == p.length()) { //*是最后一个字符
                return true;
            } else {
                char pNextChar = p.charAt(pStart + 1);
                int t = typeConvert(pNextChar);
                if (t == 2) { //字符
                    Pos pos = groupMap.get(pStart + 1);
                    for (int i : pos.matchIndex) {
                        if (i >= sStart) {
                            boolean b = isMatch(s, i + pos.len, sEnd, p, pos.start + pos.len, pEnd, groupMap);
                            if (b) {
                                return true;
                            }
                        }
                    }
                    return false;
                } else if (t == 1) { //?

                    //TODO
                    return true;
                } else {
                    System.err.println("error1");
                    return false;
                }
            }
        } else if(pFirst == '?') {
            //TODO
            return true;
        } else {
           Pos pos = groupMap.get(pStart);
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
        int preCharType = -1; //init
        for (int i = 0; i < p.length(); i++) {
                char c = p.charAt(i);
                int t = typeConvert(c);
                if (t == preCharType) {
                    sb.append(c);
                } else {
                    if (preCharType != -1 && sb.length() > 0) {
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
            if (pos.type == 2) {
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

    private void mergeQuesionMark(String s, String p, Map<Integer, Pos>  groupMap) {
        Set<Integer> startSet = groupMap.keySet();
        List<Integer> sortedList = new ArrayList<Integer>(startSet);
        Collections.sort(sortedList);
        for (int i = 0; i < sortedList.size(); i++) {
            int startIndex = sortedList.get(i);
            if (groupMap.get(startIndex).type == 1) { //?
                //left
                if (i > 0 && groupMap.get(sortedList.get(i-1)).type == 2) {

                }

            }

        }
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
        Pos(int t, int s, int len) {
            this.type = t;
            this.start = s;
            this.len = len;
        }
        int type; //0-*,1-?,2-other
        int start;//在p字符串中的开始位置
        int len;//长度
        List<Integer> matchIndex = new ArrayList(); //匹配位置

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(typeConvert(type));
            sb.append('-');
            sb.append(start);
            sb.append('-');
            sb.append(len);
            sb.append('-');
            sb.append(matchIndex);
            return sb.toString();
        }
    }
    public static void main(String[] args) {
        WildcardMatchingV2 s = new WildcardMatchingV2();
//        assert s.isMatch("adceb", "*a*b") == true;
//        long t = System.currentTimeMillis();
//        w.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
//                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb");
////        System.out.println(System.currentTimeMillis() - t);
//        assert w.isMatch("acdcb", "a*c?b") == false;
//        assert s.isMatch("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaabbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbbaaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaabaaababaaaabb",
//                "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaaa*a********ba*bbb***a*ba*bb*bb**a*b*bb") == false;
//                assert s.isMatch("", "a") == false;
//        assert s.isMatch("cb", "*a") == false;
//        assert s.isMatch("aa", "*") == true;
////        assert s.isMatch("b", "?b") == false;
//        assert s.isMatch("aa", "a") == false;
//        assert s.isMatch("adceb", "*a*b") == true;
        s.isMatch("", "???");
    }

}
