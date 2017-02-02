<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${episode.titel}</h2>
                <hr class="star-primary">
                Staffel:  <h3><a href="/season/${episode.staffelId}" class="btn btn-primary">${episode.staffelName} <i class="fa fa-arrow-circle-o-right"></i></a></h3>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Handlung</h3>
                    <hr />
                    <p>
                        ${episode.inhaltsangabe}
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Figuren</h3>
                    <hr />
                    <p>
                    <#list episode.figures as figure>
                        ${figure.name} - <a href="/${figure.typ}/${figure.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Orte</h3>
                    <hr />
                    <p>
                    <#list episode.locations as location>
                        ${location.name} <a href="/location/${location.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <hr />
                <#assign ratingData=episode.ratingData>
                <#include "rating.ftl" parse=true>
                </div>
            </div>
        </div>
    </div>
</section>