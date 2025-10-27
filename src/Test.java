void main (String[] args) {

    LocalDateTime now = LocalDateTime.now();
    String formatted = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    IO.println(formatted);
    
}