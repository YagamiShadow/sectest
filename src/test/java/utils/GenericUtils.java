package utils;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

public class GenericUtils {
    public static String composeUrl(String baseUrl, String... getParams){
        if (getParams.length == 0 || baseUrl == null){
            return baseUrl;
        }
        if (!baseUrl.endsWith("&") && !baseUrl.endsWith("?")){
            if (baseUrl.contains("?")){
                baseUrl+="&";
            } else {
                baseUrl+="?";
            }
        }
        return baseUrl+serializeParameters(getParams);
    }

    public static String serializeParameters(Object[] params){
        List<Object> pList = Arrays.asList(params);
        if (pList.size() % 2 != 0){
            pList.add("");
        }
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < params.length; i += 2) {
                builder.append(URLEncoder.encode(String.valueOf(params[i]), StandardCharsets.UTF_8.name())).append('=').append(URLEncoder.encode(String.valueOf(params[i + 1]), StandardCharsets.UTF_8.name()));
                if (params.length - i > 2) {
                    builder.append('&');
                }
            }
        } catch (UnsupportedEncodingException e){
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    public static String genRandomString(int length){

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    public static MultipartBody serializeMultipart(Object... params){
        List<Object> pList = Arrays.asList(params);
        if (pList.size() % 2 != 0){
            pList.add("");
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < pList.size(); i += 2) {
            String name = String.valueOf(pList.get(i));
            Object value = pList.get(i+1);
            if (value instanceof byte[]){
                builder.addPart(Headers.of("Content-Disposition","form-data; name=\""+name+"\"; filename=\""+name+".png\""), RequestBody.create(MediaType.parse("image/png"), (byte[]) value));
            } else {
                builder.addFormDataPart(name, String.valueOf(value));
            }
        }
        return builder.setType(MediaType.get("multipart/form-data")).build();
    }


    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(Locale.getDefault()).withZone(ZoneId.systemDefault());

    public static String dateString(int daysAdd){
        return DATE_TIME_FORMATTER.format(Instant.now().plus(daysAdd, ChronoUnit.DAYS));
    }

    public static <T> List<T> listOf(T obj, int times){
        List<T> list = new ArrayList<>(times);
        for (int i = 0; i<times; ++i){
            list.add(obj);
        }
        return list;

    }
}
