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
                        ${episode.titel} - ${episode.nummer} - ${episode.erstausstrahlungsdatum}<br />
                    </#list>
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>