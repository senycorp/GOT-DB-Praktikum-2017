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