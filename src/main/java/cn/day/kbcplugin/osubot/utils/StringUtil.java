package cn.day.kbcplugin.osubot.utils;

public class StringUtil {

    public static String camelCaseToSnakeCase(String camelCase) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char currentChar = camelCase.charAt(i);
            // 如果当前字符是大写字母且不是第一个字符，则在其前面添加下划线
            if (Character.isUpperCase(currentChar) && i > 0) {
                output.append('_');
            }
            // 将当前字符转换为小写并添加到输出字符串中
            output.append(Character.toLowerCase(currentChar));
        }
        return output.toString();
    }
}
