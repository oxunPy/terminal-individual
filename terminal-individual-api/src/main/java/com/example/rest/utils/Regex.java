package com.example.rest.utils;

import java.util.regex.Pattern;

public class Regex {
    private static final Pattern PUNCTUATIONS_PATTERN = Pattern.compile("[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]");

    public static void main(String[] args) {
        String a = "Hi , how .. .  $%#$$%^#$$@!$$#%^*^(&*(^&%^$%#? / / . are you ?";
        System.out.println(a.replaceAll("\\p{Punct}",""));
        System.out.println(a.replaceAll("[!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]",""));
    }
}
