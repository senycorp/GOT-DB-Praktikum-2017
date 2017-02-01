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