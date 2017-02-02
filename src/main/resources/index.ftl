<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="Selcuk Kekec <senycorp@googlemail.com>">

    <title>GoT WebApp</title>

    <!-- Fav Icon -->
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
    <link rel="icon" href="/favicon.ico" type="image/x-icon">

    <!-- Bootstrap Core CSS -->
    <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- Theme CSS -->
    <link href="css/freelancer.css" rel="stylesheet">

    <!-- Customization CSS -->
    <link href="css/styles.css" rel="stylesheet">


    <!-- Custom Fonts -->
    <link href="vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,700" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Lato:400,700,400italic,700italic" rel="stylesheet"
          type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body id="page-top" class="index">

<!-- Navigation -->
<nav id="mainNav" class="navbar navbar-default navbar-fixed-top navbar-custom">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header page-scroll">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span> Menu <i class="fa fa-bars"></i>
            </button>
            <a class="navbar-brand" href="/" style="float:none;">
                <img class="img-responsive" src="img/got.png" alt="" width="64" style="display:inline;">
                GoT WebApp
            </a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li class="hidden">
                    <a href="#page-top"></a>
                </li>
                <li class="page-scroll">
                    <a href="#figures">Figures</a>
                </li>
                <li class="page-scroll">
                    <a href="#häuser">Häuser</a>
                </li>
                <li class="page-scroll">
                    <a href="#seasons">Seasons</a>
                </li>
                <li class="page-scroll">
                    <a href="#playlists">Playlists</a>
                </li>
                <li>
                    <a href="#"> | <i class="fa fa-user"></i> ${userData.name}</a>
                </li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>

<!-- Header -->
<header>
    <div class="container">
        <div class="row">
            <div class="col-lg-12">
                <img class="img-responsive" src="img/got.png" alt="" width="256">
                <div class="intro-text">
                    <span class="name">GoT WebApp</span>
                    <hr class="star-light">
                    <#--<span class="skills">Create - Update - Delete</span>-->
                </div>
            </div>
        </div>
    </div>
</header>

<section id="figures">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>Figures</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <table class="table">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Titel</th>
                        <th>Heimat</th>
                        <th>Typ</th>
                        <th></th>
                    </tr>
                <#list figures as figure>
                    <#if figure.typ == "person">
                        <tr>
                            <td>${figure.id}</td>
                            <td>${figure.name}</td>
                            <td>${figure.titel}</td>
                            <td>${figure.heimat}</td>
                            <td><i class="fa fa-male"></i> Person</td>
                            <td><a href="/person/${figure.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></td>
                        </tr>
                    </#if>
                </#list>
                </table>
                <hr />
                <table class="table">
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Besitzer / Titel</th>
                        <th>Heimat</th>
                        <th>Typ</th>
                        <th></th>
                    </tr>
                <#list figures as figure>
                    <#if figure.typ == "tier">
                        <tr>
                            <td>${figure.id}</td>
                            <td>${figure.name}</td>
                            <td>${figure.besitzer} / ${figure.besitzerTitel}</td>
                            <td>${figure.heimat}</td>
                            <td><i class="fa fa-paw"></i> Tier</td>
                            <td><td><a href="/animal/${figure.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></td></td>
                        </tr>
                    </#if>
                </#list>
                </table>
                <hr />
                <div class="row">
                    <div class="col-md-2">
                        <a href="/search/figures/all" class="btn btn-primary btn-block"><i class="fa fa-bars"></i> Alle</a>
                    </div>
                    <div class="col-md-1 text-center">

                    </div>
                    <div class="col-md-6">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Suche nach...">
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button" onClick="search('figures', this)"><i class="fa fa-search"></i> Search</button>
                            </span>
                        </div><!-- /input-group -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="häuser" class="success">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>Häuser</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <table class="table">
                    <tr>
                        <th>ID</th>
                        <th>Wappen</th>
                        <th>Name</th>
                        <th>Motto</th>
                        <th>Burg</th>
                        <th>Standort</th>
                        <th></th>
                    </tr>
                <#list haueser as haus>
                    <tr>
                        <td>${haus.id}</td>
                        <td><img src="${haus.wappen}"/></td>
                        <td>${haus.name}</td>
                        <td>${haus.motto}</td>
                        <td>${haus.burg}</td>
                        <td>${haus.standort}</td>
                        <td><a href="/haus/${haus.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></td>
                    </tr>
                </#list>
                </table>
                <hr />
                <div class="row">
                    <div class="col-md-2">
                        <a href="/search/haus/all" class="btn btn-primary btn-block"><i class="fa fa-bars"></i> Alle</a>
                    </div>
                    <div class="col-md-1 text-center">

                    </div>
                    <div class="col-md-6">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Suche nach...">
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button" onClick="search('haus', this)"><i class="fa fa-search"></i> Search</button>
                            </span>
                        </div><!-- /input-group -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="seasons">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>Seasons</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <table class="table">
                    <tr>
                        <th>ID</th>
                        <th>Nummer</th>
                        <th>Start</th>
                        <th>Episodenanzahl</th>
                        <th></th>
                    </tr>
                <#list seasons as season>
                    <tr>
                        <td>${season.id}</td>
                        <td>${season.nummer}</td>
                        <td>${season.startdatum}</td>
                        <td>${season.episodenanzahl}</td>
                        <td><a href="/season/${season.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></td>
                    </tr>
                </#list>
                </table>
                <hr />
                <div class="row">
                    <div class="col-md-2">
                        <a href="/search/seasons/all" class="btn btn-primary btn-block"><i class="fa fa-bars"></i> Alle</a>
                    </div>
                    <div class="col-md-1 text-center">

                    </div>
                    <div class="col-md-6">
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="Suche nach...">
                            <span class="input-group-btn">
                                <button class="btn btn-default" type="button" onClick="search('seasons', this)"><i class="fa fa-search"></i> Search</button>
                            </span>
                        </div><!-- /input-group -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="playlists" class="success">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>Playlists</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <table class="table">
                    <tr>
                        <th>ID</th>
                        <th>Titel</th>
                        <th>Besitzer</th>
                        <th>Episodenanzahl</th>
                        <th></th>
                    </tr>
                <#list playlists as playlist>
                    <tr>
                        <td>${playlist.id}</td>
                        <td>${playlist.titel}</td>
                        <td>${userData.name}</td>
                        <td>${playlist.episodenAnzahl}</td>
                        <td><a href="/playlist/${playlist.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></td>
                    </tr>
                </#list>
                </table>
                <hr />
                <div class="row">
                    <div class="col-lg-12">
                        <form class="form-inline pull-right">
                            <div class="form-group">
                                <#--<label for="playlistTitle">Titel</label>-->
                                <input type="text" class="form-control" id="playlistTitle" placeholder="Playlist Titel...">
                            </div>
                            <button type="button" onClick="createPlaylist($('#playlistTitle').val())" class="btn btn-primary"><i class="fa fa-save"></i> Create Playlist</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="text-center">
    <div class="footer-below">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    Copyright &copy; Selcuk Kekec 2017
                </div>
            </div>
        </div>
    </div>
</footer>

<!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
<div class="scroll-top page-scroll hidden-sm hidden-xs hidden-lg hidden-md">
    <a class="btn btn-primary" href="#page-top">
        <i class="fa fa-chevron-up"></i>
    </a>
</div>

<!-- jQuery -->
<script src="vendor/jquery/jquery.min.js"></script>

<!-- Bootstrap Core JavaScript -->
<script src="vendor/bootstrap/js/bootstrap.min.js"></script>

<!-- Plugin JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-easing/1.3/jquery.easing.min.js"></script>

<!-- Contact Form JavaScript -->
<script src="js/jqBootstrapValidation.js"></script>
<script src="js/contact_me.js"></script>

<!-- Theme JavaScript -->
<script src="js/freelancer.js"></script>

<!-- Customization JavaScript -->
<script src="js/script.js"></script>

</body>

</html>
