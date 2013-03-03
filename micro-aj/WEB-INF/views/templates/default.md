<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
        "http://www.w3.org/TR/html4/strict.dtd">

<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta charset="utf-8">
    <title>Micro: $!{Tools.PathUtilities.extractName("$path")} </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="author" content="Florin T.PATRASCU">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" >    

    <link href="/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
    
    <link href='http://fonts.googleapis.com/css?family=Inconsolata' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="/styles/github.css" type="text/css" charset="utf-8" media="screen">
    <link rel="stylesheet" href="/styles/main.css" type="text/css" charset="utf-8" media="screen">

    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>

<body>
    <div class="navbar navbar-static-top">
      <div class="navbar-inner">
        <div class="container">
          $partials.get("header.html")
        </div>
      </div>
    </div>

    <div class="container">
      <div class="row">
        <div id="main" class="span8 github">
          $content.get("markdown", $path)
        </div>
        <div id="sidebar" class="span4">
          $partials.get("sidebar.html")
        </div>
      </div>
    </div>
    
    <footer>
      <div class="container">
        $partials.get("footer.html")
      </div>
    </footer>
</body>
</html>
