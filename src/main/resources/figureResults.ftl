<table class="table">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Heimat</th>
        <th>Typ</th>
        <th></th>
    </tr>
<#list rows as figure>
    <tr>
        <td>${figure.id}</td>
        <td>${figure.name}</td>
        <td>${figure.heimat}</td>
        <#if figure.typ == "person">
            <td><i class="fa fa-male"></i> Person</td>
        <#else>
            <td><i class="fa fa-paw"></i> Tier</td>
        </#if>
        <td><a href="/${figure.typ}/${figure.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></td>
    </tr>
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
