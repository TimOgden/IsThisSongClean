from flask import (
    Blueprint, flash, g, redirect, render_template, request, url_for
)

from isthissongclean.db import open_connection


bp = Blueprint('homepage', __name__)


@bp.route('/', methods=('GET', 'POST'))
def index():
    if request.method == 'POST':
        title = request.form.get('title', '')
        artist = request.form.get('artist', '')
        error = None

        if not title and not artist:
            error = 'Needs more info!'

        if error is not None:
            flash(error)
        else:
            id_ = None
            with open_connection() as db:
                db.execute('INSERT OR IGNORE INTO songs (title, artist, lyrics) VALUES (?, ?, ?)',
                           (title, artist, ''))
                id_ = db.execute('SELECT id FROM songs WHERE (title, artist) = (?, ?)',
                                 (title, artist)).fetchone()[0]
                print(id_)
            if id_ is not None:
                return redirect(url_for('homepage.song_info', id_=id_, title=title, artist=artist))
    return render_template('home/index.html')


@bp.route('/song/<int:id_>/')
def song_info(id_):
    return render_template('song/song.html')
