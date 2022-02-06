from flask import (
    Blueprint, g, request, url_for, render_template
)


bp = Blueprint('song', __name__)


@bp.route('/song/<int:id_>')
def song_info(id_):
    return render_template('song/song.html')
