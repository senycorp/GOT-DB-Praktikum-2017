<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${haus.name}</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Sitz</h3>
                    <hr />
                    <p>${haus.burgName} (${haus.ortName})</p>
                </div>
                <div class="col-lg-12">
                    <h3>Personen</h3>
                    <hr />
                    <p>
                    <#list haus.members as member>
                        ${member.name}<br />
                    </#list>
                    </p>
                </div>
                <div class="col-lg-12">
                    <h3>Besitz</h3>
                    <hr />
                    <p>
                    <#list haus.properties as property>
                    ${property.name}<br />
                    </#list>
                    </p>
                </div>
            </div>
        </div>
    </div>
</section>