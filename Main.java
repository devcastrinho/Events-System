import controller.EventoController;
import controller.UsuarioController;
import view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        EventoController eventoCtrl = new EventoController();
        UsuarioController usuarioCtrl = new UsuarioController();
        ConsoleView view = new ConsoleView(eventoCtrl, usuarioCtrl);
        view.iniciar();
    }
}
