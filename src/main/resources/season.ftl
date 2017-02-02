<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${season.nummer}</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Episoden</h3>
                    <hr />
                    <p>
                    <#list season.episodes as episode>
                        ${episode.titel} - ${episode.nummer} - ${episode.erstausstrahlungsdatum} <a href="/episode/${episode.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                    </#list>
                    </p>
                </div>
            </div>
            <div class="col-lg-12">
                <hr />
            <#assign ratingData=season.ratingData>
            <#include "rating.ftl" parse=true>
            </div>
        </div>
    </div>
</section>