package com.example.kmucab.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.example.kmucab.Domain.TourData;
import com.example.kmucab.Repository.TourDataRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class TourApiService {

    private final TourDataRepository tourDataRepository;

    public TourApiService(TourDataRepository tourDataRepository) {
        this.tourDataRepository = tourDataRepository;
    }

    public void fetchAndStoreTourData() {
        try {
            // URL 객체 생성
            String apiUrl = "https://apis.data.go.kr/B551011/KorService1/";
            String areaBasedList1 = "areaBasedList1";
            String detailCommon1 = "detailCommon1";
            String searchFestival1 = "searchFestival1";
            String serviceKey = "8UzfKVaCH%2F0u36pSQn7qhGKq3RcRcS16Vjn6Kf%2FluXmkNqastLHkxVpxC%2BBPcINqQpSKyRfH68be6ihKo3PZzQ%3D%3D";
            for (int no = 1; no <= 6; no++) {
                String fullUrl = apiUrl + areaBasedList1 + "?serviceKey=" + serviceKey + "&MobileOS=ETC&MobileApp=AppTest&_type=json&arrange=D&numOfRows=10000&pageNo=" + no;

                System.out.println(fullUrl);

                // URL 객체를 사용하여 HTTP 연결 설정
                URL url = new URL(fullUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // 응답 코드 확인
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // 응답 데이터 읽기
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // 응답 데이터 출력
                    System.out.println("API Response: " + response);

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");


                    JSONObject jsonObject = new JSONObject(response.toString());  // 여기서 응답 문자열을 JSON 객체로 변환

                    // JSON 파싱 및 데이터 처리
                    if (jsonObject.has("response")) {
                        JSONObject responseObj = jsonObject.getJSONObject("response");
                        JSONObject body = responseObj.getJSONObject("body");
                        JSONObject items = body.getJSONObject("items");
                        JSONArray itemArray = items.getJSONArray("item");

                        // 데이터를 MariaDB에 저장
                        for (int i = 0; i < itemArray.length(); i++) {
                            JSONObject item = itemArray.getJSONObject(i);
                            TourData tourData = new TourData();
                            // 필수 값 처리
                            if (item.has("contentid")) {
                                tourData.setContentid(item.getInt("contentid"));
                            } else {
                                System.err.println("필수 값인 contentid가 없습니다. 데이터를 건너뜁니다.");
                                continue;
                            }

                            if (item.has("contenttypeid")) {
                                tourData.setContenttypeid(item.getInt("contenttypeid"));
                            } else {
                                System.err.println("필수 값인 contenttypeid가 없습니다. 데이터를 건너뜁니다.");
                                continue;
                            }

                            if (item.has("modifiedtime")) {
                                try {
                                    tourData.setModifiedtime(formatter.parse(item.getString("modifiedtime")));
                                } catch (ParseException e) {
                                    System.err.println("modifiedtime의 날짜 포맷이 잘못되었습니다. 데이터를 건너뜁니다.");
                                    continue;
                                }
                            } else {
                                System.err.println("필수 값인 modifiedtime이 없습니다. 데이터를 건너뜁니다.");
                                continue;
                            }

                            if (item.has("title")) {
                                tourData.setTitle(item.getString("title"));
                            } else {
                                System.err.println("필수 값인 title이 없습니다. 데이터를 건너뜁니다.");
                                continue;
                            }

                            // 선택적 값 처리
                            tourData.setAreaCode(item.has("areacode") && !item.getString("areacode").isEmpty()
                                    ? Integer.parseInt(item.getString("areacode"))
                                    : 0); // null일 때 0 사용
                            tourData.setSingunguCode(item.has("sigungucode") && !item.getString("sigungucode").isEmpty()
                                    ? item.getInt("sigungucode")
                                    : 0); // null일 때 0 사용
                            tourData.setMapx(item.has("mapx") && !item.getString("mapx").isEmpty()
                                    ? item.getString("mapx")
                                    : null);
                            tourData.setMapy(item.has("mapy") && !item.getString("mapy").isEmpty()
                                    ? item.getString("mapy")
                                    : null);
                            tourData.setAddress(item.has("addr1") && !item.getString("addr1").isEmpty()
                                    ? item.getString("addr1")
                                    : null);
                            tourData.setTel(item.has("tel") && !item.getString("tel").isEmpty()
                                    ? item.getString("tel")
                                    : null);
                            System.out.println(tourData);
                            tourDataRepository.save(tourData);
                        }
                    } else {
                        System.out.println("Response key not found in JSON data");
                    }


                } else {
                    System.out.println("GET 요청 실패: " + responseCode);
                }
            }
            } catch(Exception e){
                e.printStackTrace();
        }
    }
}
