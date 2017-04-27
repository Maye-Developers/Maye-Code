<?php
require_once("baglan.php");
if ($_POST) {
  $id = $_POST["secim"];

  $sql = "SELECT * FROM Adim WHERE id=".$id;
  $sonuc = mysqli_query($con,$sql);
  $oku = mysqli_fetch_assoc($sonuc);

  $baslik = $oku["baslik"];
  $icerik = $oku["icerik"];
  $katId = $oku["kategoriId"];

  $sql = "SELECT * FROM DogruSonuc WHERE adimId=".$id;
  $sonuc = mysqli_query($con,$sql);
  $i=0;
  $a=0;
  while ($a < 5) {
    $dizi[$a] = "";
    $a++;
  }
  while ($oku = mysqli_fetch_assoc($sonuc)) {
    if($oku["syntax"] != ""){
      $dizi[$i] = $oku["syntax"];
    }
    else {
      $dizi[$i] = "";
    }
    $i++;
  }


?>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin - Bootstrap Admin Template</title>

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <div id="wrapper">

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html">SB Admin</a>
            </div>
            <!-- Top Menu Items -->
            <ul class="nav navbar-right top-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i> John Smith <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li>
                            <a href="#"><i class="fa fa-fw fa-user"></i> Profile</a>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-envelope"></i> Inbox</a>
                        </li>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-gear"></i> Settings</a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a href="#"><i class="fa fa-fw fa-power-off"></i> Log Out</a>
                        </li>
                    </ul>
                </li>
            </ul>
            <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
            <div class="collapse navbar-collapse navbar-ex1-collapse">
                <ul class="nav navbar-nav side-nav">
                    <li>
                        <a href="index.php"><i class="fa fa-fw fa-edit"></i> İçerik Ekle</a>
                    </li>
                    <li class="active">
                        <a href="duzenle.php"><i class="fa fa-fw fa-pencil"></i> Düzenle</a>
                    </li>
                </ul>
            </div>
            <!-- /.navbar-collapse -->
        </nav>

        <div id="page-wrapper">

            <div class="container-fluid">

                <!-- Page Heading -->
                <div class="row">
                    <div class="col-lg-12">
                        <h1 class="page-header">
                            İçerik Düzenle
                        </h1>
                        <ol class="breadcrumb">
                            <li>
                                <i class="fa fa-dashboard"></i>  <a href="index.html">Admin</a>
                            </li>
                            <li class="active">
                                <i class="fa fa-edit"></i> Düzenle
                            </li>
                        </ol>
                    </div>
                </div>
                <!-- /.row -->
              <form method="post" action="secimkaydet.php">
                <?php
                echo '<input type="hidden" name="id" value="'.$id.'">';
                ?>
                <div class="row">
                    <div class="col-lg-6">

                        <form role="form">

                            <div class="form-group">
                                <label>Başlık</label>
                                <?php
                                  echo '<input class="form-control" value="'.$baslik.'" name="baslik">';
                                ?>
                            </div>

                            <div class="form-group">
                                <label>İçerik</label>
                                <?php
                                echo '<textarea class="form-control" name="icerik" rows="3">'.$icerik.'</textarea>';
                                ?>
                            </div>
                            <div class="form-group">
                                <label>Kategori</label>
                                <select class="form-control" name="kategori">
                                    <?php
                                      $sql ="SELECT * FROM Kategori";//mail ve şifre kontrolü
                                      $sonuc = mysqli_query($con,$sql);
                                      while ($oku = mysqli_fetch_assoc($sonuc)) {
                                        if ($oku["id"] == $katId) {
                                          echo "<option selected value='".$oku["id"]."'>".$oku["adi"]."</option>";
                                        }
                                        else {
                                          echo "<option value='".$oku["id"]."'>".$oku["adi"]."</option>";
                                        }
                                      }
                                    ?>
                                </select>
                            </div>
                        </form>

                    </div>
                    <div class="col-lg-6">

                        <div class="form-group">
                            <label>Doğru Cevap Alternatifleri</label>
                            <?php
                              echo '<input name="alt1" class="form-control" value="'.$dizi[0].'" placeholder="Alternatif #1">';
                             ?>
                        </div>
                        <div class="form-group">
                          <?php
                            echo '<input name="alt2" class="form-control" value="'.$dizi[1].'" placeholder="Alternatif #2">';
                           ?>
                        </div>
                        <div class="form-group">
                          <?php
                            echo '<input name="alt3" class="form-control" value="'.$dizi[2].'" placeholder="Alternatif #3">';
                           ?>
                        </div>
                        <div class="form-group">
                          <?php
                            echo '<input name="alt4" class="form-control" value="'.$dizi[3].'" placeholder="Alternatif #4">';
                           ?>
                        </div>
                        <div class="form-group">
                          <?php
                            echo '<input name="alt5" class="form-control" value="'.$dizi[4].'" placeholder="Alternatif #5">';
                           ?>
                        </div>
                        <button type="submit" style="float:right;" class="btn btn-default">Kaydet</button>
                    </div>
                </div>
                <!-- /.row -->
              </form>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->
</body>

</html>
<?php
}
else{
  header("Location: duzenle.php");
}
?>
