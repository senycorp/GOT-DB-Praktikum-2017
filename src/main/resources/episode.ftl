<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${episode.titel}</h2>
                <hr class="star-primary">
                Staffel:  <h3>${episode.staffelName}</h3>
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
                        ${figure.name} - ${figure.typ}<br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Orte</h3>
                    <hr />
                    <p>
                    <#list episode.locations as location>
                    ${location.name}<br />
                    </#list>
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>