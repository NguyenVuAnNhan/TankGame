Đây là project trên trường của cháu cho bộ môn lập trình hướng đối tượng bằng java ạ.
Cháu vừa viết xong dự án 3 ngày trước ạ, thời gian cháu viết là 6 tuần, chia thành 3 tuần suy nghĩ, 2 tuần lập trình và 1 tuần thiết kế test ạ.

Đây là một bản của game na ná giống Gunny ạ, ta có các cơ chế sau:

+ Sau khi bắn, đổi lượt ngay lập tức
+ Đạn nổ gây sát thương và xe tăng rơi không có dù cũng gây sát thương, cộng điểm bằng sát thương cho người bắn đạn
* Gây sát thương cho bản thân không được tính điểm
+ Điểm được dùng để quyết định người thắng hoặc dùng một số chiêu tốn điểm
+ Sau khi chỉ còn 1 xe tăng trên bàn chơi, game sẽ chuyển level
+ Sau 3 level, game sẽ kết thúc, hiện scoreboard cuối mà hiện người chơi được sort theo số điểm giảm dần
+ Khi game kết thúc, bấm r để restart một ván game mới

Các hành động có thể của một xe tăng:

+ mũi tên trái phải: di chuyển, tốn xăng, nếu không có xăng không cho di chuyển
+ SPACE: bắn một viên đạn pháo cơ bản, kết thúc lượt
+ phím 1: bắn một viên đạn lớn mà nổ thành một chùm 5 viên bom bi, tiêu 30 điểm, kết thúc lượt
+ phím 2: bắn một chùm 5 viên bom bi như một phát súng săn, tiêu 30 điểm, kết thúc lượt
+ mũi tên lên xuống: xoay góc của nòng súng xe tăng
+ phím w, s: tăng giảm power của xe tăng, ảnh hưởng lên vận tốc đầu nòng của các phát bắn
+ phím r: hồi máu xe tăng 20 điểm, giới hạn 100, tiêu 20 điểm
+ phím f: hồi xăng xe tăng 200 điểm, không giới hạn số lượng, tiêu 10 điểm
+ phím p: cho xe tăng thêm 1 dù, không giới hạn số lượng, tiêu 15 điểm. Dù miễn nhiễm sát thương rơi của xe tăng
+ phím h: cho xe tăng bật khiên, tiêu 20 điểm. Khiên miễn nhiễm sát thương nổ từ một viên pháo