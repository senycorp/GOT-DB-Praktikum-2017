<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${playlist.titel}</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Episoden</h3>
                    <hr />
                    <#list playlist.episodes.seasons as season>
                        <h3>${season}</h3>
                        <hr />
                        <#list playlist.episodes.data as episode>
                        <#if episode.staffelNummer == season >
                        ${episode.titel} - ${episode.nummer} - ${episode.erstausstrahlungsdatum} <a href="/episode/${episode.id}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a><br />
                        </#if>
                        </#list>
                    </#list>
                </div>
            </div>
        </div>
    </div>
</section>