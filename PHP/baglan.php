<?php
define('DB_NAME', '');//kendi database adınız
/** MySQL veritabanı kullanıcısı */
define('DB_USER', '');//kendi kullanıcı adınız
/** MySQL veritabanı parolası */
define('DB_PASSWORD', '');//kendi şifreniz
/** MySQL sunucusu */
define('DB_HOST', 'localhost');
/** Yaratılacak tablolar için veritabanı karakter seti. */
define('DB_CHARSET', 'latin5_turkish_ci');
$con=mysqli_connect(DB_HOST,DB_USER,DB_PASSWORD,DB_NAME);

// Check connection
if (mysqli_connect_errno())
  {
  echo "Bağlanırken hata oluştu : " . mysqli_connect_error();
  }

?>
