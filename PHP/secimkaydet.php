<?php
require_once("baglan.php");
if ($_POST) {
  $adimId = $_POST["id"];
  $baslik = $_POST["baslik"];
  $icerik = $_POST["icerik"];
  $alt1 = $_POST["alt1"];
  $alt2 = $_POST["alt2"];
  $alt3 = $_POST["alt3"];
  $alt4 = $_POST["alt4"];
  $alt5 = $_POST["alt5"];
  $kategori = $_POST["kategori"];

  if ($baslik !="" && $icerik != "") {
    $sql="UPDATE Adim SET baslik = '$baslik', icerik = '$icerik', kategoriId = '$kategori' WHERE id='$adimId'";

    if (mysqli_query($con,$sql)){
      $sql ="DELETE FROM DogruSonuc WHERE adimId=".$adimId;
      mysqli_query($con,$sql);
      if ($alt1 != "") {
        $sql="INSERT INTO DogruSonuc (adimId, syntax) VALUES ('$adimId','$alt1')";
        mysqli_query($con,$sql);
      }
      if ($alt2 != "") {
        $sql="INSERT INTO DogruSonuc (adimId, syntax) VALUES ('$adimId','$alt2')";
        mysqli_query($con,$sql);
      }
      if ($alt3 != "") {
        $sql="INSERT INTO DogruSonuc (adimId, syntax) VALUES ('$adimId','$alt3')";
        mysqli_query($con,$sql);
      }
      if ($alt4 != "") {
        $sql="INSERT INTO DogruSonuc (adimId, syntax) VALUES ('$adimId','$alt4')";
        mysqli_query($con,$sql);
      }
      if ($alt5 != "") {
        $sql="INSERT INTO DogruSonuc (adimId, syntax) VALUES ('$adimId','$alt5')";
        mysqli_query($con,$sql);
      }
    }else{
        $sonucmesaji = "Kayıt Başarılı değil.";
    }
    mysqli_close($con);//database bağlantısını kapattık
  }
  header("Location: duzenle.php");
}
 ?>
