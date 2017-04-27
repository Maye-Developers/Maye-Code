<?php
require_once("baglan.php");
if($_POST){
  $kullanici = $_POST["kullanici"];
  $sifre = $_POST["sifre"];
  $mail = $_POST["mail"];
  $id = $_POST["id"];

  $sql= "UPDATE Kullanici SET mail='".$mail."',kadi='".$kullanici."', sifre='".md5($sifre)."' WHERE id=".$id;
  $sonuc = mysqli_query($con,$sql);

if($sonuc == 1){

    echo '{"sonuc":"1"}';
}
  else {
    echo '{"sonuc":"0"}';
  }
}else {
  echo "hata";
}
 ?>
