<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${location.name}</h2>
                <hr class="star-primary">
                <#if location.haus??>aktuell in Besitz von <h3>${location.haus.name}</h3></#if>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Burg</h3>
                    <hr />
                    <p>
                        ${location.burgName}
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Personen</h3>
                    <hr />
                    <p>
                    <#list location.persons as person>
                        ${person.name} <a href="/person/${person.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Episoden</h3>
                    <hr />
                    <p>
                    <#list location.episodes as episode>
                    ${episode.staffelName} ${episode.episodeName} <a href="/episode/${episode.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>