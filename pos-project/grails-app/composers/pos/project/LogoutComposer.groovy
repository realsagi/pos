package pos.project


class LogoutComposer extends zk.grails.Composer {

    def afterCompose = { 

        def logout   = $d('#logout')[0]
        def username = $d('#username')[0]

        $d('#logout').on('click', {
            alert("test")
        })

    }
}
